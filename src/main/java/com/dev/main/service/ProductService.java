package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.CategoryDto;
import com.dev.main.dto.ProductDto;
import com.dev.main.dto.VariationDto;
import com.dev.main.model.Product;

public interface ProductService {
	List<Product> getAllProducts();
	List<String> getAllMaterials();
	List<String> getAllOccasion();
	List<String> getAllTheme();
	List<Product> getAllForTable();
	List<Product> getAllProductsWithCategories();
	Product getProductById(Long id);
	Product getProductWithCategoryById(Long id);
	void createForTable(ProductDto productDto, CategoryDto categoryDto, VariationDto variationDto);
	void editProductWithCategory(Long id,ProductDto productDto, CategoryDto cateogry);
	Product createProduct(ProductDto productDto);
}
