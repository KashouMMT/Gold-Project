package com.dev.main.utils;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.dev.main.model.AppSecret;
import com.dev.main.repository.AppSecretRepository;
import com.dev.main.security.MyUserDetailsService;

@Configuration
public class MyRememberMeConfigurer {
	
	private static final Logger logger = LoggerFactory.getLogger(MyRememberMeConfigurer.class);
	private static final String SECRET_NAME = "remember_me_key";
	
    @Value("${security.rememberme.key:}")   // can be set via REMEMBER_ME_KEY env var
    private String configuredKey;

	@Value("${security.rememberme.token-validity}")
	private int tokenValiditySeconds;
	
    private final MyUserDetailsService myUserDetailsService;
    private final PersistentTokenRepository tokenRepository;
    private final AppSecretRepository appSecretRepo;
	
	public MyRememberMeConfigurer(MyUserDetailsService myUserDetailsService, PersistentTokenRepository tokenRepository,AppSecretRepository appSecretRepo) {
		this.myUserDetailsService = myUserDetailsService;
		this.tokenRepository = tokenRepository;
		this.appSecretRepo = appSecretRepo;
	}

    public void configure(HttpSecurity http) throws Exception {
        String key = resolveRememberMeKey();

        http.rememberMe(rememberMe -> {
            logger.info("Configuring Remember-Me");
            logger.debug("tokenValiditySeconds={}", tokenValiditySeconds);
            rememberMe
                .key(key)
                .rememberMeParameter("remember-me")
                .tokenRepository(tokenRepository)
                .tokenValiditySeconds(tokenValiditySeconds)
                .userDetailsService(myUserDetailsService);
        });
    }
    
    private String resolveRememberMeKey() {
        // 1) Highest priority: explicit property/env
        if (configuredKey != null && !configuredKey.isBlank()) {
            String k = configuredKey.trim();
            upsertIfDifferent(k); // keep DB in sync (optional)
            logger.info("Using remember-me key from configuration/environment.");
            return k;
        }
        // 2) DB 
        Optional<AppSecret> fromDb = appSecretRepo.findById(SECRET_NAME);
        if (fromDb.isPresent() && fromDb.get().getValue() != null && !fromDb.get().getValue().isBlank()) {
            logger.info("Loaded remember-me key from database.");
            return fromDb.get().getValue().trim();
        }
        // 3) Generate once and persist
        String generated = generateKey();
        try {
            appSecretRepo.save(new AppSecret(SECRET_NAME, generated, LocalDateTime.now()));
            logger.warn("No remember-me key configured; generated and stored in DB.");
            return generated;
        } catch (DataIntegrityViolationException race) {
            // another instance inserted it first — re-read
            String v = appSecretRepo.findById(SECRET_NAME)
                    .map(AppSecret::getValue)
                    .orElse(generated);
            logger.info("Detected concurrent insert; using remember-me key from DB.");
            return v;
        }
    }
    
    private void upsertIfDifferent(String newValue) {
        appSecretRepo.findById(SECRET_NAME).ifPresentOrElse(existing -> {
            if (!newValue.equals(existing.getValue())) {
                existing.setValue(newValue);
                appSecretRepo.save(existing);
            }
        }, () -> appSecretRepo.save(new AppSecret(SECRET_NAME, newValue,LocalDateTime.now())));
    }
    
    private String generateKey() {
        byte[] bytes = new byte[32]; // 256-bit
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static PersistentTokenRepository createTokenRepository(DataSource dataSource) {
        var tokenRepo = new org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);

        // Check if the table already exists before setting create-on-startup
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, "persistent_logins", new String[] {"TABLE"})) {
                if (!rs.next()) {
                    // Table not found, safe to auto-create
                    tokenRepo.setCreateTableOnStartup(true);
                    System.out.println("[RememberMe] persistent_logins table not found. It will be auto-created.");
                } else {
                    System.out.println("[RememberMe] persistent_logins table already exists. Skipping creation.");
                }
            }
        } catch (SQLException e) {
            System.err.println("[RememberMe] Failed to check if persistent_logins table exists: " + e.getMessage());
        }

        return tokenRepo;
    }
}
