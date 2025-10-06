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

import com.dev.main.model.Category;
import com.dev.main.model.Product;
import com.dev.main.model.User;
import com.dev.main.security.MyUserDetails;
import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private ProductService productService;
	
	private CategoryService categoryService;
	
	public AdminController(ProductService productService, CategoryService categoryService) {
		this.productService = productService;
		this.categoryService = categoryService;
	}
	
	public String authentication(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			logger.warn("Unauthenticated access attempt to /admin/dashboard");
			
			return "failed";
		}
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		
		logger.debug("Authenticated admin user: name={}, email={}, role={}",user.getName(),user.getEmail(),user.getRoles());
		
		return user.getName();
	}
	
	@GetMapping({"/dashboard",""})
	public String adminDashboardPage(Model model, Authentication authentication) {
		logger.info("GET request received for /admin/dashboard");
		
		String username = authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		model.addAttribute("username",username);
		logger.debug("Added attribute to model: key='username', value='{}'", username);
		logger.info("Rendering admin dashboard for user: {}", username);
		
		model.addAttribute("content","admin/content/admin-dashboard");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/product")
	public String productListPage(Model model,Authentication authentication) {
		logger.info("GET request received for /admin/product");
		
		String username = authentication(authentication);
		
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
	
	@GetMapping("/category")
	public String categoryListPage(Model model,Authentication authentication) {
		
		logger.info("GET request received for /admin/category");
		
		String username = authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<String> columns = List.of("ID","Category_Name","Description");
		logger.debug("CategoryList Page Columns defined: {}", columns);
		
		List<Category> categories = categoryService.getAllCategories();
		logger.info("Retrieved {} categories from categoryService", categories.size());
		
		List<Map<String,Object>> items = categories.stream()
			.map(c -> {
				Map<String, Object> m = new LinkedHashMap<>();
				m.put("id", c.getId());
				m.put("category_name", c.getCategory_name());
				m.put("description", c.getDescription());
				return m;
			}).collect(Collectors.toList());
		logger.debug("Transformed {} categories into display maps", items.size());
		model.addAttribute("items",items);
		logger.debug("Added attribute to model: key='items', value='{}'", items.toString());
		model.addAttribute("columns",columns);
		logger.debug("Added attribute to model: key='columns', value='{}'", columns.toString());
		model.addAttribute("table_name","Category Table");
		logger.debug("Added attribute to model: key='table_name', value='{}'", "Category Table");
		model.addAttribute("username",username);
		logger.debug("Added attribute to model: key='username', value='{}'", username);
		model.addAttribute("content","admin/content/admin-tables");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/test")
	public String adminTestPage(Model model) {
		logger.info("GET request received for /admin/test");
		
		model.addAttribute("content","admin/content/admin-static");
		
		return "admin/admin-layout";
	}
}
