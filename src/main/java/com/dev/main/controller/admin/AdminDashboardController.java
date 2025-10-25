package com.dev.main.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.dev.main.utils.OtherUtility;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
	
	private final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
	
	private OtherUtility otherUtility;
	
	public AdminDashboardController(OtherUtility otherUtility) {
		this.otherUtility = otherUtility;
	}
	
	@GetMapping({"","/"})
	public String redirectToAdminDashboard() {
		logger.info("GET request received for /admin");
		
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/dashboard")
	public String adminDashboardPage(Model model, Authentication authentication,SessionStatus status) {
		logger.info("GET request received for /admin/dashboard");
		status.setComplete();
		logger.debug("Session Attribute cleared");
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		model.addAttribute("username",username);
		logger.debug("Added attribute to model: key='username', value='{}'", username);
		logger.info("Rendering admin dashboard for user: {}", username);
		
		model.addAttribute("content","admin/content/admin-dashboard");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/test")
	public String adminTestPage(Model model) {
		logger.info("GET request received for /admin/test");
		
		model.addAttribute("content","admin/content/admin-static");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/admin-form")
	public String adminFormTestPage(Model model) {
		logger.info("GET request received for /admin/admin-form");
		
		model.addAttribute("content","admin/content/admin-form");
		
		return "admin/admin-layout";
	}
	

}
