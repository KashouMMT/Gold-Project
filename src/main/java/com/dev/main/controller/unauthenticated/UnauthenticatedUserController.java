package com.dev.main.controller.unauthenticated;

import java.awt.print.Pageable;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.main.model.Product;
import com.dev.main.service.ProductImageService;
import com.dev.main.service.ProductLengthService;
import com.dev.main.service.ProductService;
import com.dev.main.service.ProductWidthService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/home")
public class UnauthenticatedUserController {
	
	private final Logger logger = LoggerFactory.getLogger(UnauthenticatedUserController.class);
	
	private final ProductService productService;
	private final ProductImageService productImageService;
	private final ProductWidthService productWidthService;
	private final ProductLengthService productLengthService;

	public UnauthenticatedUserController(ProductService productService, ProductImageService productImageService,
			ProductWidthService productWidthService, ProductLengthService productLengthService) {
		super();
		this.productService = productService;
		this.productImageService = productImageService;
		this.productWidthService = productWidthService;
		this.productLengthService = productLengthService;
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
	
	@GetMapping("/cart")
	public String cartPage(Model model) {
		
		model.addAttribute("content","public/content/cart");
		
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
		model.addAttribute("productImage",productImageService.getFirstProductImageByProductId(productId));
		model.addAttribute("product",productService.getProductById(productId));
		model.addAttribute("content","public/content/item");
		
		return "public/public-layout";
	}
}
