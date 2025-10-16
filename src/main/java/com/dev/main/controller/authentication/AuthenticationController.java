package com.dev.main.controller.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.dto.UserDto;
import com.dev.main.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
	
	private final UserService userService;
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping({"/login","/login?error"})
	public String loginPage() {
		logger.info("GET request received for /auth/login");
		logger.info("Rendering login page");
		return "auth/login";
	}
	
	@GetMapping("/access-denied")
	public String accessDeniedPage() {
		logger.info("GET request received for /auth/access-denied");
		logger.info("Rendering access denied page");
		return "auth/access-denied";
	}
	
	@GetMapping("/error")
	public String errorPage() {
		logger.info("GET request received for /auth/error");
		logger.info("Rendering error page");
		return "auth/error";
	}
	
	@GetMapping("/sign-up")
	public String signUpPage(Model model) {
		logger.info("GET request received for /auth/sign-up");
		
		UserDto userDto = new UserDto();
		model.addAttribute("userDto",userDto);
		
		logger.debug("Added attribute to model: key='role', value='{}'", userDto.getClass().getSimpleName());
		logger.info("Rendering sign up page");
		return "auth/sign-up";
	}
	
	@PostMapping("/register")
	public String registerNewAccount(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result,Model model) {
		logger.info("POST /auth/register called to create user");
		
		if(result.hasErrors()) {
			logger.error("Error on POST /auth/register. Redirecting back to /auth/sign-up",userDto);
			model.addAttribute("userDto",userDto);
			logger.debug("Added attribute to model: key='role', value='{}'", userDto.getClass().getSimpleName());
			logger.info("Rendering sign up page");

			return "auth/sign-up";
		}
		
		logger.debug("Payload received: {}",userDto);
		
		userService.createUser(userDto);
		
		logger.info("User successfully created");
		logger.info("Redirecting back to /auth/login");
		
		return "redirect:/auth/login";
	}
}
