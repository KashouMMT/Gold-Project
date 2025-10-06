package com.dev.main.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.dev.main.security.MyUserDetailsService;

import jakarta.annotation.PostConstruct;

@Configuration
public class MyRememberMeConfigurer {

	private static final Path KEY_PATH = Paths.get("keys/rememberme.key");
	
	private String rememberMeKey;

	@Value("${security.rememberme.token-validity}")
	private int tokenValiditySeconds;
	
	private final MyUserDetailsService myUserDetailsService;
	private final PersistentTokenRepository tokenRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(MyRememberMeConfigurer.class);
	
	public MyRememberMeConfigurer(MyUserDetailsService myUserDetailsService, PersistentTokenRepository tokenRepository) {
			this.myUserDetailsService = myUserDetailsService;
			this.tokenRepository = tokenRepository;
	}

	public void configure(HttpSecurity http) throws Exception {
		http.rememberMe(rememberMe -> {
			logger.info("Configuring Remember-Me functionality");
			logger.debug("Remember-Me configuration details: tokenValiditySeconds={}",tokenValiditySeconds);
			rememberMe
			.key(rememberMeKey)
			.rememberMeParameter("remember-me")
			.tokenRepository(tokenRepository)
			.tokenValiditySeconds(tokenValiditySeconds)
			.userDetailsService(myUserDetailsService);	
		});
	}

    public static PersistentTokenRepository createTokenRepository(DataSource dataSource) {
    	logger.debug("Initializing PersistentTokenRepository for Remember-Me functionality");
	    JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();	
	    tokenRepo.setDataSource(dataSource);
	    logger.debug("Checking if 'persistent_logins' table exists before enabling auto-creation");
	    if (!tableExists(dataSource, "persistent_logins")) {
	    	logger.info("'persistent_logins' table not found. Creating it on startup");
	        tokenRepo.setCreateTableOnStartup(true);
	    } else {
	        logger.debug("'persistent_logins' table already exists. Skipping auto-creation");
	    }
	    return tokenRepo;
	}
    
	private static boolean tableExists(DataSource dataSource, String tableName) {
		logger.debug("Verifying existence of table: {}", tableName);
	    try (Connection conn = dataSource.getConnection()) {
	        DatabaseMetaData meta = conn.getMetaData();
	        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
	        	boolean exists = rs.next();
	        	logger.debug("Table '{}' existence check result: {}", tableName, exists);
	            return exists;
	        }
	    } catch (SQLException e) {
	    	logger.error("Error occurred while checking if table '{}' exists: {}", tableName, e.getMessage(), e);
	        throw new RuntimeException("Failed to check if table exists", e);
	    }
	}
	
	@PostConstruct
	public void init() {
	    if (Files.exists(KEY_PATH)) {
	        try {
	            rememberMeKey = Files.readString(KEY_PATH).trim();
	            logger.info("Loaded existing Remember-Me key from file.");
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to read Remember-Me key from file", e);
	        }
	    } else {
	        rememberMeKey = generateRememberMeKey();
	        try {
	            Files.writeString(KEY_PATH, rememberMeKey, StandardOpenOption.CREATE_NEW);
	            logger.info("Generated and saved new Remember-Me key to file.");
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to write Remember-Me key to file", e);
	        }
	    }
	}
	
	private String generateRememberMeKey() {
	    try {
	        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
	        byte[] keyBytes = new byte[32];  // 256 bits
	        secureRandom.nextBytes(keyBytes);
	        return Base64.getEncoder().encodeToString(keyBytes);
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Failed to generate Remember Me key", e);
	    }
	}
}
