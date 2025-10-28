package com.dev.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dev.main.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	Optional<Category> findByCategoryName(String categoryName);
	
	@EntityGraph(attributePaths = "products")
	@Query("select distinct c from Category c order by c.id asc")
	List<Category> findAllWithProducts();
	
	Long countById(Long id);
}
