package com.dev.main.controller.unauthenticated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class UnauthenticatedUserController {
	
	private final Logger logger = LoggerFactory.getLogger(UnauthenticatedUserController.class);
	
	@GetMapping({"","/"})
	public String homePage() {
		logger.info("GET request received for /home");
		logger.info("Rendering home page");
		return "public/home";
	}
	
	@GetMapping("/about")
	public String aboutPage() {
		logger.info("GET request received for /home/about");
		logger.info("Rendering about page");
		return "public/about";
	}
}
