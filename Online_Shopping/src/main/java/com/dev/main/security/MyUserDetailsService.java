package com.dev.main.security;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dev.main.model.User;
import com.dev.main.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

	private final UserRepository userRepo;
	
	public MyUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		logger.info("Attempting to load user by email: {}", email);
		
		Optional<User> user = userRepo.findByEmail(email);
		if(user.isEmpty()) {
			logger.warn("User with email [{}] not found in the database.", email);
			throw new UsernameNotFoundException("User is not found.");
		}
		
		logger.debug("User found: name={}, roles={}", user.get().getName(), user.get().getRoles());
        logger.info("Successfully loaded user with email: {}", email);
		
		return new MyUserDetails(user.get());
	}

}
