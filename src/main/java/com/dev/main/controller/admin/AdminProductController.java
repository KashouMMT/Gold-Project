package com.dev.main.controller.admin;

import java.util.List;

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

import com.dev.main.dto.CategoryDto;
import com.dev.main.dto.ProductDto;
import com.dev.main.dto.ProductLengthDto;
import com.dev.main.dto.ProductWidthDto;
import com.dev.main.dto.VariationDto;
import com.dev.main.model.Product;
import com.dev.main.model.ProductLength;
import com.dev.main.model.ProductWidth;
import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductLengthService;
import com.dev.main.service.ProductService;
import com.dev.main.service.ProductWidthService;
import com.dev.main.utils.FieldSpec;
import com.dev.main.utils.OtherUtility;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminProductController {
	
    private final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
	
    private ProductService productService;
	private CategoryService categoryService;
	private ProductWidthService productWidthService;
	private ProductLengthService productLengthService;
	private OtherUtility otherUtility;
	
	public AdminProductController(ProductService productService,
		CategoryService categoryService,
		ProductWidthService productWidthService,
		OtherUtility otherUtility,
		ProductLengthService productLengthService) {
		this.productService = productService;
		this.otherUtility = otherUtility;
		this.categoryService = categoryService;
		this.productWidthService = productWidthService;
		this.productLengthService = productLengthService;
	}
	
	@GetMapping("/product")
	public String productListPage(Model model,Authentication authentication) {
		logger.info("GET request received for /admin/product");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<Product> products = productService.getAllForTable();
		model.addAttribute("products",products);
		model.addAttribute("username",username);
		model.addAttribute("content","admin/content/admin-product-tables");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-product")
	public String addProductPage(Model model, Authentication authentication) {
		logger.info("GET request received for /admin/add-category");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		ProductDto productDto = new ProductDto();
		CategoryDto categoryDto = new CategoryDto();
		model.addAttribute("productDto",productDto);
		model.addAttribute("categoryDto",categoryDto);
		model.addAttribute("materials",productService.getAllMaterials());
		model.addAttribute("occasions",productService.getAllOccasion());
		model.addAttribute("themes",productService.getAllTheme());
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("addAction",true);
		model.addAttribute("content","admin/content/admin-add-product");
		model.addAttribute("formAction","/admin/add-product");

	    return "admin/admin-layout";
	}
	
	@PostMapping("/add-product")
	public String postAddProductPage(
		@Valid @ModelAttribute("productDto") ProductDto productDto,
		BindingResult result1,
        @Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
        BindingResult result2,
        @ModelAttribute VariationDto variationDto,
        Model model) {
		
		if(result1.hasErrors() || result2.hasErrors()) {
			model.addAttribute("productDto",productDto);
			model.addAttribute("categoryDto",categoryDto);
		    
			model.addAttribute("materials",productService.getAllMaterials());
			model.addAttribute("occasions",productService.getAllOccasion());
			model.addAttribute("themes",productService.getAllTheme());
			model.addAttribute("categories",categoryService.getAllCategories());
			model.addAttribute("addAction",true);
			model.addAttribute("formAction","/admin/add-product");
			
			model.addAttribute("content","admin/content/admin-add-product");
			
			return "admin/admin-layout";
		}
		
		productService.createForTable(productDto, categoryDto, variationDto);
		
		return "redirect:/admin/product";
	}
	
	@GetMapping("edit-product/{id}")
	public String editProductPage(@PathVariable Long id,
		Model model,Authentication authentication) {
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		ProductDto productDto = new ProductDto();
		CategoryDto categoryDto = new CategoryDto();
		Product product = productService.getProductWithCategoryById(id);
		productDto.setTitle(product.getTitle());
		productDto.setDescription(product.getDescription());
		productDto.setMaterial(product.getMaterial());
		productDto.setOccasion(product.getOccasion());
		productDto.setTheme(product.getTheme());
		productDto.setCategory(product.getCategory());
		categoryDto.setCategoryName(product.getCategory().getCategoryName());
		
		model.addAttribute("productDto",productDto);
		model.addAttribute("categoryDto",categoryDto);
		model.addAttribute("materials",productService.getAllMaterials());
		model.addAttribute("occasions",productService.getAllOccasion());
		model.addAttribute("themes",productService.getAllTheme());
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("username",username);
		model.addAttribute("formTitle","Edit Product");
		model.addAttribute("formAction",String.format("/admin/edit-product/%d",id));
		
		model.addAttribute("content","admin/content/admin-add-product");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/edit-product/{id}")
	public String postEditProductPage(@Valid @ModelAttribute("productDto") ProductDto productDto,
			BindingResult result1,
	        @Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
	        BindingResult result2,
	        @PathVariable Long id,
	        Model model) {
		
		if(result1.hasErrors() || result2.hasErrors()) {			
			model.addAttribute("productDto",productDto);
			model.addAttribute("categoryDto",categoryDto);
			model.addAttribute("materials",productService.getAllMaterials());
			model.addAttribute("occasions",productService.getAllOccasion());
			model.addAttribute("themes",productService.getAllTheme());
			model.addAttribute("categories",categoryService.getAllCategories());
			model.addAttribute("formTitle","Edit Product");
			model.addAttribute("formAction",String.format("/admin/edit-product/%d",id));
			
			model.addAttribute("content","admin/content/admin-add-product");
			
			return "admin/admin-layout";
		}
		
		productService.editProductWithCategory(id, productDto, categoryDto);
		
		return "redirect:/admin/product";
	}
	
	@GetMapping("/product/view-variation/{id}")
	public String viewVariationPage(@PathVariable Long id,Model model,Authentication authentication) {
		
		logger.info("GET request received for /admin/view-variation/{id}");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<ProductWidth> productWidths = productWidthService.getProductWidthByProductId(id);
		
		model.addAttribute("product",productWidths.stream().map(t -> t.getProduct()).findFirst().orElse(null));
		model.addAttribute("productWidths",productWidths);
		model.addAttribute("content","admin/content/admin-product-variation-table");
		model.addAttribute("username",username);
		
		return "admin/admin-layout";
	}

	@GetMapping("/product/view-variation/edit-width/{productId}/{widthId}")
	public String editWidthPage(@PathVariable Long productId,
		@PathVariable Long widthId, Model model,Authentication authentication) {
		
		logger.info("GET request received for /admin/view-variation/{id}");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		ProductWidth productWidth = productWidthService.getProductWidthById(widthId);
		ProductWidthDto productWidthDto = new ProductWidthDto();
		productWidthDto.setWidth(productWidth.getWidth());
		
		model.addAttribute("objectDto",productWidthDto);
		model.addAttribute("productId",productId);
		model.addAttribute("widthId",widthId);
		model.addAttribute("content","admin/content/admin-form");
		
	    List<FieldSpec> fields = List.of(
	    		new FieldSpec("width", "Width", "number", true, null, null, null)
		);
	    model.addAttribute("fields", fields);
		model.addAttribute("formTitle","Edit Width");
		model.addAttribute("formAction",String.format("/admin/product/view-variation/edit-width/%d/%d",productId,widthId));
	    model.addAttribute("submitLabel","Save Wdith");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/product/view-variation/edit-width/{productId}/{widthId}")
	public String postEdithWidthPage(@Valid @ModelAttribute("objectDto") ProductWidthDto productWidthDto,
		BindingResult result, 
		@PathVariable Long productId, @PathVariable Long widthId,
		Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto", productWidthDto);
			model.addAttribute("content","admin/content/admin-product-variation-edit");
			model.addAttribute("productId",productId);
			model.addAttribute("widthid",widthId);
			
		    List<FieldSpec> fields = List.of(
		    		new FieldSpec("width", "Width", "number", true, null, null, null)
			);
		    model.addAttribute("fields", fields);
			model.addAttribute("formTitle","Edit Width");
			model.addAttribute("formAction",String.format("/admin/product/view-variation/edit-width/%d/%d",productId,widthId));
		    model.addAttribute("submitLabel","Save Wdith");
			
			return "admin/admin-layout";
		}
		productWidthService.editProductWidth(widthId, productWidthDto);
		
		return String.format("redirect:/admin/product/view-variation/%d",productId);
	}
	
	@GetMapping("/product/view-variation/edit-length/{productId}/{widthId}/{lengthId}")
	public String editLengthPage(@PathVariable Long productId,
			@PathVariable Long widthId,
			@PathVariable Long lengthId,
			Model model) {
		
		ProductLength productLength = productLengthService.getProductLengthById(lengthId);
		ProductLengthDto productLengthDto = new ProductLengthDto();
		productLengthDto.setLength(productLength.getLength());
		productLengthDto.setPrice(productLength.getPrice());
		
		model.addAttribute("objectDto",productLengthDto);
		model.addAttribute("productId",productId);
		model.addAttribute("widthId",widthId);
		model.addAttribute("content","admin/content/admin-form");
		
	    List<FieldSpec> fields = List.of(
	    		new FieldSpec("length", "Length", "number", true, null, null, null),
	    		new FieldSpec("price", "Price", "number", true, null, null, null)
		);
	    model.addAttribute("fields", fields);
		model.addAttribute("formTitle","Edit Variation");
		model.addAttribute("formAction",String.format("/admin/product/view-variation/edit-length/%d/%d/%d",productId,widthId,lengthId));
	    model.addAttribute("submitLabel","Save Variation");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/product/view-variation/edit-length/{productId}/{widthId}/{lengthId}")
	public String postEdithLengthPage(@Valid @ModelAttribute("objectDto") ProductLengthDto productLengthDto,
		BindingResult result, 
		@PathVariable Long productId, 
		@PathVariable Long widthId,
		@PathVariable Long lengthId,
		Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto", productLengthDto);
			model.addAttribute("content","admin/content/admin-form");
			model.addAttribute("productId",productId);
			model.addAttribute("widthid",widthId);
			
		    List<FieldSpec> fields = List.of(
		    		new FieldSpec("length", "Length", "number", true, null, null, null),
		    		new FieldSpec("price", "Price", "number", true, null, null, null)
			);
		    model.addAttribute("fields", fields);
			model.addAttribute("formTitle","Edit Variation");
			model.addAttribute("formAction",String.format("/admin/product/view-variation/edit-length/%d/%d/%d",productId,widthId,lengthId));
		    model.addAttribute("submitLabel","Save Variation");
			
			return "admin/admin-layout";
		}
		productLengthService.editProductLength(lengthId, productLengthDto);
		
		return String.format("redirect:/admin/product/view-variation/%d",productId);
	}
}
