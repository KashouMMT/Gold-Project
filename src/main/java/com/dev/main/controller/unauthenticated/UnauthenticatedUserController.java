package com.dev.main.controller.unauthenticated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class UnauthenticatedUserController {
	
	private final Logger logger = LoggerFactory.getLogger(UnauthenticatedUserController.class);
	
	@GetMapping({"","/"})
	public String homePage(Model model) {
		logger.info("GET request received for /home");
		logger.info("Rendering home page");
		
		model.addAttribute("content","public/content/home");
		
		return "public/public-layout";
	}
	
	@GetMapping("/about")
	public String aboutPage(Model model) {
		logger.info("GET request received for /home/about");
		logger.info("Rendering about page");
		
		model.addAttribute("content","public/content/about");
		
		return "public/public-layout";
	}
	
	@GetMapping("/collection")
	public String collecionPage(Model model) {
		logger.info("GET request received for /home/collection");
		logger.info("Rendering about page");
		
		model.addAttribute("content","public/content/collection");
		
		return "public/public-layout";
	}
}
