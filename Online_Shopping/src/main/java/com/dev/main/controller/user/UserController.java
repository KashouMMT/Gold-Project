package com.dev.main.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.model.User;
import com.dev.main.security.MyUserDetails;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/dashboard")
	public String homePage(Model model,Authentication authentication) {
		logger.info("GET request received for /user/dashboard");
		
		if (authentication == null || !authentication.isAuthenticated()) {
			logger.warn("Unauthenticated access attempt to /user/dashboard");
			
			return "redirect:/auth/login";
		}
		
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		
		logger.debug("Authenticated user: name={}, email={}, role={}",user.getName(),user.getEmail(),user.getRoles());
		
		model.addAttribute("role",user.getName());
		
		logger.debug("Added attribute to model: key='role', value='{}'",user.getName());
		logger.info("Rendering user dashboard for user: {}",user.getName());
		
		return "user/user-dashboard";
	}
}
