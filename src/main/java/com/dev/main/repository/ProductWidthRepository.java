package com.dev.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.main.model.ProductWidth;

@Repository
public interface ProductWidthRepository extends JpaRepository<ProductWidth, Long>{
	
	@EntityGraph(attributePaths = {"productLengths"})
	Optional<ProductWidth> findById(Long id);
	
	
	@EntityGraph(attributePaths = {"product", "productLengths"})
	List<ProductWidth> findByProduct_IdOrderByIdAsc(Long productId);
}
