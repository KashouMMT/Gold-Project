package com.dev.main.controller.unauthenticated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.main.service.CartService;
import com.dev.main.service.ProductImageService;
import com.dev.main.service.ProductLengthService;
import com.dev.main.service.ProductService;
import com.dev.main.service.ProductWidthService;
import com.dev.main.utils.AuthenticationUtility;

@Controller
@RequestMapping("/home")
public class UnauthenticatedUserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UnauthenticatedUserController.class);
	
	private final ProductService productService;
	private final ProductImageService productImageService;
	private final ProductWidthService productWidthService;
	private final ProductLengthService productLengthService;
	private final CartService cartService;
	private final AuthenticationUtility authenticationUtility;

	public UnauthenticatedUserController(ProductService productService, ProductImageService productImageService,
			ProductWidthService productWidthService, ProductLengthService productLengthService, CartService cartService,
			AuthenticationUtility authenticationUtility) {
		super();
		this.productService = productService;
		this.productImageService = productImageService;
		this.productWidthService = productWidthService;
		this.productLengthService = productLengthService;
		this.cartService = cartService;
		this.authenticationUtility = authenticationUtility;
	}

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
		
		model.addAttribute("products",productService.getAllWithCategoryAndImage());
		model.addAttribute("content","public/content/collection");
		
		return "public/public-layout";
	}
	

	
	@GetMapping("/item/{productId}")
	public String itemPage(@PathVariable("productId") Long productId,
		@RequestParam(name = "widthId",required = false)Long widthId,
		@RequestParam(name = "lengthId",required = false)Long lengthId,
		Model model) {
		
		if(widthId != null) model.addAttribute("width",productWidthService.getProductWidthById(widthId));
		if(lengthId != null) {
			model.addAttribute("width",productWidthService.getProductWidthById(widthId));
			model.addAttribute("length",productLengthService.getProductLengthById(lengthId));
		}
		model.addAttribute("productImages",productImageService.getAllProductImagesByProductId(productId));
		model.addAttribute("product",productService.getProductById(productId));
		model.addAttribute("content","public/content/item");
		
		return "public/public-layout";
	}
	
	@PostMapping("/item/{productId}")
	public String postItemPage(@PathVariable("productId") Long productId,
		@RequestParam(name = "widthId")Long widthId,
		@RequestParam(name = "lengthId")Long lengthId,
		@RequestParam(name = "quantity")int quantity,
		Authentication authentication,
		RedirectAttributes redirectAttr,
		Model model) {
		
		String username = authenticationUtility.authentication(authentication);
		if (username.equals("failed") || !authenticationUtility.isUser(authentication)) {
			redirectAttr.addFlashAttribute("warn","Please login as user to proceed with checkout.");
			return "redirect:/auth/login";
		}
		cartService.createCartAndCartItemIfNotExist(authenticationUtility.getUser(authentication),productId,widthId,lengthId,quantity);
		
		return "redirect:/home/collection";
	}
}
