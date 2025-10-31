package com.dev.main.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.main.model.User;
import com.dev.main.security.MyUserDetails;
import com.dev.main.service.CartService;
import com.dev.main.utils.AuthenticationUtility;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private final CartService cartService;
	private final AuthenticationUtility utility;	

	public UserController(CartService cartService, AuthenticationUtility utility) {
		super();
		this.cartService = cartService;
		this.utility = utility;
	}

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
	
	@GetMapping("/cart")
	public String cartPage(Authentication authentication,
		RedirectAttributes redirectAttr,
		Model model) {
	
		if (utility.authentication(authentication).equals("failed")) {
			logger.warn("Unauthenticated access attempt to /user/dashboard");	
			redirectAttr.addFlashAttribute("warn","Please login.");
			return "redirect:/auth/login";
		}
		
		if (!utility.isUser(authentication)) {
			logger.warn("Admin attempt to access cart");
			redirectAttr.addFlashAttribute("warn","Admin cannot buy products");
			return "redirec:/auth/login";
		}
		
		model.addAttribute("cart",cartService.getCartViewByUserId(utility.getUser(authentication).getId()));
		model.addAttribute("content","public/content/cart");
		
		return "public/public-layout";
	}
}
