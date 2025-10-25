package com.dev.main.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.main.dto.CategoryDto;
import com.dev.main.model.Category;
import com.dev.main.repository.CategoryRepository;
import com.dev.main.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	private final CategoryRepository categoryRepo;
	
	public CategoryServiceImpl(CategoryRepository categoryRepo) {
		this.categoryRepo = categoryRepo;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Category> getAllCategories() {
		logger.info("Fetching all categories from the database.");
		List<Category> categories = categoryRepo.findAll(Sort.by(Sort.Direction.ASC,"categoryName").and(Sort.by("id")));
		logger.debug("Total categories retrieved: {}", categories.size());
		return categories;
	}

	@Override
	@Transactional
	public Category createCategory(CategoryDto categoryDto) {
		Category category = new Category();
		category.setCategoryName(categoryDto.getCategoryName());
		return categoryRepo.save(category);
	}

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepo.findById(id).orElse(null);
	}

	@Override
	public Category getCategoryByCategoryName(String categoryName) {
		return categoryRepo.findByCategoryName(categoryName).orElse(null);
	}

	@Override
	public boolean isCategoryPresent(CategoryDto categoryDto) {
		return categoryRepo.findByCategoryName(categoryDto.getCategoryName()).isPresent();
	}

	@Override
	public List<Category> getAllCategoriesWithProducts() {
		return categoryRepo.findAllWithProducts();
	}
}
