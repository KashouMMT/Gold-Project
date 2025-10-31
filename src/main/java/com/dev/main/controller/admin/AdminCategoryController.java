package com.dev.main.controller.admin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.main.dto.CategoryDto;
import com.dev.main.model.Category;
import com.dev.main.service.CategoryService;
import com.dev.main.utils.FieldSpec;
import com.dev.main.utils.AuthenticationUtility;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);
	
	private AuthenticationUtility otherUtility;
	
	private CategoryService categoryService;
	
	public AdminCategoryController(CategoryService categoryService, AuthenticationUtility otherUtility) {
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
		
		List<String> columns = List.of("ID","Category_Name","Products","Actions");
		List<Category> categories = categoryService.getAllCategoriesWithProducts();
	    Map<String, String> buttonActions = new HashMap<>();
	    buttonActions.put("editAction", "/admin/edit-category/{id}");
	    buttonActions.put("editActionText", "Edit Category");
	    buttonActions.put("deleteAction", "/admin/delete-category/{id}");
	    buttonActions.put("deleteActionText", "Delete Category");
		List<Map<String,Object>> items = categories.stream()
			.map(c -> {
				Map<String, Object> m = new LinkedHashMap<>();
				m.put("id", c.getId());
				m.put("category_name", c.getCategoryName());
				m.put("products",c.getProducts());
				m.put("actions", buttonActions);
				return m;
			}).collect(Collectors.toList());
	    model.addAttribute("addAction", "/admin/add-category");
	    model.addAttribute("addActionText", "Add New Category");
	    model.addAttribute("items",items);
		model.addAttribute("columns",columns);
		model.addAttribute("tableName","Category Table");
		model.addAttribute("username",username);
		model.addAttribute("content","admin/content/admin-tables");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-category")
	public String addCategoryPage(Model model, Authentication authentication) {
		logger.info("GET request received for /admin/add-category");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		CategoryDto categoryDto = new CategoryDto();
		
		model.addAttribute("objectDto",categoryDto);
		
		List<FieldSpec> fields = List.of(
			new FieldSpec("categoryName", "Category name", "text", true, 150, null, null)
		);
		model.addAttribute("fields",fields);
		
		model.addAttribute("formAction","/admin/add-category");
		model.addAttribute("submitLabel","Save Category");
		model.addAttribute("formTitle","Add Category");
		model.addAttribute("tableName","Category Table");
		model.addAttribute("tableLink","/admin/category");
		
		
		model.addAttribute("content","admin/content/admin-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/add-category")
	public String postAddCategoryPage(@Valid @ModelAttribute("objectDto") CategoryDto categoryDto,
			BindingResult result,Model model) {
		
		if (result.hasErrors()) {
		    // re-add the same form metadata the GET adds
		    model.addAttribute("fields", List.of(
		        new FieldSpec("categoryName", "Category name", "text", true, 150, null, null)
		    ));
		    model.addAttribute("formAction","/admin/add-category");
		    model.addAttribute("submitLabel","Save Category");
		    model.addAttribute("formTitle","Add Category");
		    model.addAttribute("content","admin/content/admin-form");
			model.addAttribute("tableName","Category Table");
			model.addAttribute("tableLink","/admin/category");
		    return "admin/admin-layout";   
		}
		categoryService.createCategory(categoryDto);
		
		return "redirect:/admin/category";
	}
	
	@GetMapping("/delete-category/{id}")
	public String deleteCategory(@PathVariable Long id,RedirectAttributes ra) {
		boolean deleted = categoryService.deleteCategoryIfEmpty(id);
        if (!deleted) {
            ra.addFlashAttribute("error",
                "Cannot delete: this category still has products attached. Remove or reassign them first.");
        } else {
            ra.addFlashAttribute("success", "Category deleted successfully.");
        }
        return "redirect:/admin/category";
	}
	
	@GetMapping("/edit-category/{id}")
	public String editCategoryPage(@PathVariable Long id, Model model) {
		
		CategoryDto categoryDto = new CategoryDto();
		Category category = categoryService.getCategoryById(id);
		categoryDto.setCategoryName(category.getCategoryName());
		
		model.addAttribute("objectDto",categoryDto);
		model.addAttribute("content","admin/content/admin-form");
		
	    List<FieldSpec> fields = List.of(
	    	new FieldSpec("categoryName", "Category Name", "text", true, 100, null, null)
		);
	    model.addAttribute("fields", fields);
		model.addAttribute("formTitle","Edit Variation");
		model.addAttribute("formAction",String.format("/admin/edit-category/%d",id));
	    model.addAttribute("submitLabel","Save Variation");
		model.addAttribute("tableName","Category Table");
		model.addAttribute("tableLink","/admin/category");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/edit-category/{id}")
	public String postEditCategoryPage(@Valid @ModelAttribute("objectDto") CategoryDto categoryDto,
		BindingResult result, @PathVariable Long id, Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto",categoryDto);
			model.addAttribute("content","admin/content/admin-form");
		    List<FieldSpec> fields = List.of(
		    	new FieldSpec("categoryName", "Category Name", "text", true, 100, null, null)
			);
		    model.addAttribute("fields", fields);
			model.addAttribute("formTitle","Edit Variation");
			model.addAttribute("formAction",String.format("/admin/edit-category/%d",id));
		    model.addAttribute("submitLabel","Save Variation");
			model.addAttribute("tableName","Category Table");
			model.addAttribute("tableLink","/admin/category");
			return "admin/admin-layout";
		}
		
		categoryService.editCategory(id, categoryDto);
		
		return "redirect:/admin/category";
	}
}
