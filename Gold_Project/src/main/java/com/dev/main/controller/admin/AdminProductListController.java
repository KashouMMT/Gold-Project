package com.dev.main.controller.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.model.Product;
import com.dev.main.service.ProductService;
import com.dev.main.utils.OtherUtility;

@Controller
@RequestMapping("/admin")
public class AdminProductListController {
	
private final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
	
	private ProductService productService;
	
	private OtherUtility otherUtility;
	
	public AdminProductListController(ProductService productService,OtherUtility otherUtility) {
		this.productService = productService;
		this.otherUtility = otherUtility;
	}
	
	@GetMapping("/product")
	public String productListPage(Model model,Authentication authentication) {
		logger.info("GET request received for /admin/product");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<String> columns = List.of("ID","Title","Summary","Price");
		logger.debug("ProductList Page Columns defined: {}", columns);
		
		List<Product> products = productService.getAllProducts();
		logger.info("Retrieved {} products from productService", products.size());
		
		List<Map<String,Object>> items = products.stream()
			.map(p -> {
				Map<String,Object> m = new LinkedHashMap<>();
				m.put("id", p.getId());
	            m.put("title", p.getTitle());
	            m.put("price", p.getPrice());
	            m.put("category", p.getCategory() != null ? p.getCategory().getCategory_name() : null);
	            return m;
			}).collect(Collectors.toList());
		logger.debug("Transformed {} products into display maps", items.size());

		model.addAttribute("items",items);
		logger.debug("Added attribute to model: key='items', value='{}'", items.toString());
		model.addAttribute("columns",columns);
		logger.debug("Added attribute to model: key='columns', value='{}'", columns.toString());
		model.addAttribute("table_name","Product Table");
		logger.debug("Added attribute to model: key='table_name', value='{}'", "Product Table");
		model.addAttribute("username",username);
		logger.debug("Added attribute to model: key='username', value='{}'", username);
		logger.info("Rendering admin dashboard for user: {}", username);
		
		model.addAttribute("content","admin/content/admin-tables");
		
		
		return "admin/admin-layout";
	}
}
