package com.dev.main.controller.admin;

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
@RequestMapping("/admin")
public class AdminController {
	
	private final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@GetMapping("/dashboard")
	public String adminDashboardPage(Model model, Authentication authentication) {
		logger.info("GET request received for /admin/dashboard");
		
		if (authentication == null || !authentication.isAuthenticated()) {
			logger.warn("Unauthenticated access attempt to /admin/dashboard");
			
			return "redirect:/auth/login";
		}
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		
		logger.debug("Authenticated admin user: name={}, email={}, role={}",user.getName(),user.getEmail(),user.getRoles());
		
		model.addAttribute("role",user.getName());
		
		logger.debug("Added attribute to model: key='role', value='{}'", user.getName());
		logger.info("Rendering admin dashboard for user: {}", user.getName());
		
		model.addAttribute("content","admin/content/admin-dashboard");
		
		return "admin/admin-layout";
	}
}
