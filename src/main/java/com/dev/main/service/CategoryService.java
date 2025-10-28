package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.CategoryDto;
import com.dev.main.model.Category;

public interface CategoryService {
	List<Category> getAllCategories();
	List<Category> getAllCategoriesWithProducts();
	Category getCategoryById(Long id);
	Category getCategoryByCategoryName(String categoryName);
	boolean isCategoryPresent(CategoryDto categoryDto);
	Category createCategory(CategoryDto categoryDto);
	boolean deleteCategoryIfEmpty(Long id);
	void editCategory(Long id,CategoryDto categoryDto);
}
