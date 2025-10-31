package com.dev.main.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;

import com.dev.main.model.Role;
import com.dev.main.model.User;
import com.dev.main.security.MyUserDetails;

@Component
public class AuthenticationUtility {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationUtility.class);

    public String authentication(Authentication authentication) {
        if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            logger.warn("Unauthenticated access attempt to /admin/dashboard");
            return "failed";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof MyUserDetails mud) {
            User user = mud.getUser();
            logger.debug("Authenticated admin user: name={}, email={}, role={}",
                    user.getName(), user.getEmail(), user.getRoles());
            return user.getName();
        }

        logger.warn("Unexpected principal type: {}", principal.getClass().getName());
        return "failed";
    }
    
    public boolean isAdmin(Authentication authentication) {
    	Object principal = authentication.getPrincipal();
    	if(principal instanceof MyUserDetails myUserDetail) {
    		logger.info("User is admin");
    		return myUserDetail.isAdmin();
    	}
    	logger.warn("User is not admin");
    	return false;
    }
    
    public boolean isUser(Authentication authentication) {
    	Object principal = authentication.getPrincipal();
    	if(principal instanceof MyUserDetails myUserDetail) {
    		logger.info("User is authenticated");
    		return myUserDetail.isUser();
    	}
    	logger.warn("User is not authenticated");
    	return false;
    }
    
    public User getUser(Authentication authentication) {
    	MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
    	return userDetails.getUser();
    }
}
