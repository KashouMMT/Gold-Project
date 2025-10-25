package com.dev.main.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.dev.main.components.CustomAccessDeniedHandler;
import com.dev.main.components.CustomAuthenticationEntryPoint;
import com.dev.main.components.CustomAuthenticationFailureHandler;
import com.dev.main.components.CustomAuthenticationSuccessHandler;
import com.dev.main.repository.AppSecretRepository;
import com.dev.main.repository.UserRepository;
import com.dev.main.security.MyUserDetailsService;
import com.dev.main.utils.MyRememberMeConfigurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepo;
    private final DataSource dataSource;
    private final AppSecretRepository appSecretRepo;
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    SecurityConfig(UserRepository userRepo, MyUserDetailsService myUserDetailsService,DataSource dataSource,AppSecretRepository appSecretRepo) {
    	this.userRepo = userRepo;
    	this.myUserDetailsService = myUserDetailsService;
    	this.dataSource = dataSource;
    	this.appSecretRepo = appSecretRepo;
    }

	@Bean
	SecurityFilterChain filter(HttpSecurity http,MyRememberMeConfigurer rememberMeConfigurer)throws Exception {
		logger.info("Initializing security filter chain.");
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.httpBasic(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> {
			logger.info("Configuring public and secure endpoints");
				auth 
				.requestMatchers("/",
								 "/public/**","/public/assets/**","/public/assets/images/**",
									          "/public/frontend/**","/public/frontend/css/**","/public/frontend/js/**",
							     "/private/**","/private/product-images","/private/user-images",
							     "/home","/home/**","/auth/**")
					.permitAll()
				.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
				.requestMatchers("/user/**").hasAuthority("ROLE_USER")
				.anyRequest().authenticated();
			})		
			.formLogin(form -> {
			logger.info("Custom login form setup at /auth/login");
			logger.debug("Login Page: /auth/login, Login processing URL: /auth/sign-in");
			logger.debug("Registering SuccessHandler: {}",authenticationSuccessHandler().getClass().getSimpleName());
			logger.debug("Registering FailureHandler: {}",authenticationFailureHandler().getClass().getSimpleName());
				form
				.loginPage("/auth/login")
				.loginProcessingUrl("/auth/sign-in")
				.successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler())
				.permitAll();
			})
			.logout(logout -> {
			logger.info("Logout configuration applied");
			logger.debug("Logut processing URL: /auth/logout, SuccessURL: /auth/login?logout");
			logger.debug("Logout configuration details: clearAuthentication=true, invalidateHttpSession=true");
				logout
				.logoutUrl("/auth/logout")
				.logoutSuccessUrl("/auth/login?logout")
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.permitAll();
			})
			.sessionManagement(session -> {
			logger.info("Session management configured: max 1 session per user, allows re-login, expired sessions redirect to /auth/login?expired");
			logger.debug("Session configuration details: maximumSessions=1, maxSessionsPreventsLogin=false, expiredUrl=/auth/login?expired");
				session
				.maximumSessions(1)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/auth/login?expired");
			})
			.exceptionHandling(exceptionHandling -> {
			logger.info("Setting up exception handlers.");
			logger.debug("Registering AccessDeniedHandler: {}", accessDeniedHandler().getClass().getName());
		    logger.debug("Registering AuthenticationEntryPoint: {}", authenticationEntryPoint().getClass().getName());
				exceptionHandling
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint());
			});
		logger.info("Configuring Remember-Me functionality");
		rememberMeConfigurer.configure(http);
		return http.build();
	}
	
	@Bean
	MyUserDetailsService userDetailsService() {
		return new MyUserDetailsService(userRepo);
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
	
    @Bean
    AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
    	provider.setPasswordEncoder(passwordEncoder());
    	return provider;
    }
	
	@Bean
	AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
	
	@Bean
	AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
	
	@Bean
	AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}
	
	@Bean
	PersistentTokenRepository persistentTokenRepository(Environment env) {
	    return MyRememberMeConfigurer.createTokenRepository(dataSource);
	}

	@Bean
	MyRememberMeConfigurer myRememberMeConfigurer(PersistentTokenRepository tokenRepo) {
	    return new MyRememberMeConfigurer(
	        myUserDetailsService,
	        tokenRepo,
	        appSecretRepo
	    );
	}
}
