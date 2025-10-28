package com.dev.main.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	Product getProductById(Long id);
	Product getProductWithCategoryById(Long id);
	void createForTable(ProductDto productDto, CategoryDto categoryDto, VariationDto variationDto,MultipartFile[] images);
	void editProductWithCategory(Long id,ProductDto productDto, CategoryDto cateogry,MultipartFile[] images);
	Product createProduct(ProductDto productDto,MultipartFile[] images);
	void deleteProduct(Long id);
}
