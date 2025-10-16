package com.dev.main.controller.unauthenticated;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dev.main.model.User;
import com.dev.main.security.MyUserDetails;

@Controller
public class StartController {
	
	private static final Logger logger = LoggerFactory.getLogger(StartController.class);
	
	@GetMapping({"","/"})
	public String redirectStartController(Model model,Authentication authentication) {
		
		logger.info("GET request received for Default URL at: {}",new java.util.Date());
		
		if (authentication == null || !authentication.isAuthenticated()) {
			logger.warn("Unauthenticated access attempt to /admin/dashboard");
			return "redirect:/auth/login";
		}
		
		MyUserDetails userDetails = (MyUserDetails)authentication.getPrincipal();
		User user = userDetails.getUser();
		
		Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		if(authorities.contains("ROLE_ADMIN")) {
			logger.debug("Authenticated admin user: name={}, email={}, role={}",user.getName(),user.getEmail(),user.getRoles());
			return "redirect:/admin/dashboard";
		}
		
		if(authorities.contains("ROLE_USER")) {
			logger.debug("Authenticated user: name={}, email={}, role={}",user.getName(),user.getEmail(),user.getRoles());
			model.addAttribute("content","user/content/user-dashboard");
			return "user/user-layout";
		}
		
		logger.debug("Authenticated admin user: name={}, email={}, role=[UNKNOWN ROLE]",user.getName(),user.getEmail(),user.getRoles());
		return "redirect:/home";
	}
}
