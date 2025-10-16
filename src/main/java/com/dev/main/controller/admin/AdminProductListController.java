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

import com.dev.main.dto.ProductDto;
import com.dev.main.model.Product;
import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductService;
import com.dev.main.utils.FieldSpec;
import com.dev.main.utils.OtherUtility;

@Controller
@RequestMapping("/admin")
public class AdminProductListController {
	
private final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
	
	private ProductService productService;
	
	private CategoryService categoryService;
	
	private OtherUtility otherUtility;
	
	public AdminProductListController(ProductService productService,OtherUtility otherUtility,CategoryService categoryService) {
		this.productService = productService;
		this.otherUtility = otherUtility;
		this.categoryService = categoryService;
	}
	
	@GetMapping("/product")
	public String productListPage(Model model,Authentication authentication) {
		logger.info("GET request received for /admin/product");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<String> columns = List.of("ID","Title","Summary","Price","Category");
		logger.debug("ProductList Page Columns defined: {}", columns);
		
		List<Product> products = productService.getAllProducts();
		logger.info("Retrieved {} products from productService", products.size());
		
		List<Map<String,Object>> items = products.stream()
			.map(p -> {
				Map<String,Object> m = new LinkedHashMap<>();
				m.put("id", p.getId());
	            m.put("title", p.getTitle());
	            m.put("summary", p.getSummary());
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
		
		Map<String,String> buttonActions = Map.of(
			"addAction","/admin/add-product",
			"addActionText","Add Product",
			"editAction","/admin/edit-product",
			"editActionText","Edit Product",
			"deleteAction","/admin/delete-product",
			"deleteActionText","Delete Product"
		);
		
		model.addAttribute("buttonActions",buttonActions);
		logger.debug("Added attribute to model: key='buttonActions', value='{}'", buttonActions.size());
		
		logger.info("Rendering admin dashboard for user: {}", username);
		model.addAttribute("content","admin/content/admin-tables");
		
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-product")
	public String addProductPage(Model model, Authentication authentication) {
		logger.info("GET request received for /admin/add-category");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		model.addAttribute("objectDto",new ProductDto());
		
	    // Options for selects
	    model.addAttribute("formOptions", Map.of(
	        "categories", categoryService.getAllCategories() // List<Category>
	    ));
		
		List<FieldSpec> fields = List.of(
		        new FieldSpec("title", "Title", "text", true, 150, null, null),
		        new FieldSpec("summary", "Summary", "textarea", false, 255, null, null),
		        new FieldSpec("price", "Price", "number", true, null, "0.01", null),
		        new FieldSpec("category", "Category", "select", true, null, null, "categories")
			);
		
		model.addAttribute("fields", fields);

	    // Where to POST and button label
		model.addAttribute("formAction","/admin/add-product");
		model.addAttribute("submitLabel","Save Product");
		model.addAttribute("formTitle","Add Product");
		
		model.addAttribute("content","admin/content/admin-form");

	    return "admin/admin-layout";
	}
}
