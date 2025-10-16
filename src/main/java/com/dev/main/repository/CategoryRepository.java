package com.dev.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.main.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}
