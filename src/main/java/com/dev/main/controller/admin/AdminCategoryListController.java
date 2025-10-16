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

import com.dev.main.dto.CategoryDto;
import com.dev.main.model.Category;
import com.dev.main.service.CategoryService;
import com.dev.main.utils.FieldSpec;
import com.dev.main.utils.OtherUtility;

@Controller
@RequestMapping("/admin")
public class AdminCategoryListController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminCategoryListController.class);
	
	private OtherUtility otherUtility;
	
	private CategoryService categoryService;
	
	public AdminCategoryListController(CategoryService categoryService, OtherUtility otherUtility) {
		this.categoryService = categoryService;
		this.otherUtility = otherUtility;
	}
	
	@GetMapping("/category")
	public String categoryListPage(Model model,Authentication authentication) {
		
		logger.info("GET request received for /admin/category");
		
		String username = otherUtility.authentication(authentication);
		
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
	
	@GetMapping("/add-category")
	public String addProductPage(Model model, Authentication authentication) {
		logger.info("GET request received for /admin/add-category");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		model.addAttribute("objectDto",new CategoryDto());
		
		List<FieldSpec> fields = List.of(
			new FieldSpec("category_name", "Category name", "text", true, 150, null, null),
			new FieldSpec("description", "Description", "textarea", false, 255, null, null)
		);
		model.addAttribute("fields",fields);
		
		model.addAttribute("formAction","/admin/add-category");
		model.addAttribute("submitLabel","Save Category");
		model.addAttribute("formTitle","Add Category");
		
		model.addAttribute("content","admin/content/admin-form");
		
		return "admin/admin-layout";
	}
}
