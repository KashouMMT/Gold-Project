package com.dev.main.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dev.main.model.Category;
import com.dev.main.repository.CategoryRepository;
import com.dev.main.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	private CategoryRepository categoryRepo;
	
	public CategoryServiceImpl(CategoryRepository categoryRepo) {
		this.categoryRepo = categoryRepo;
	}

	@Override
	public List<Category> getAllCategories() {
		logger.info("Fetching all categories from the database.");
		List<Category> categories = categoryRepo.findAll();
		logger.debug("Total categories retrieved: {}", categories.size());
		return categories;
	}
}
