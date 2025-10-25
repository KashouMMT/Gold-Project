package com.dev.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dev.main.model.ProductWidth;

@Repository
public interface ProductWidthRepository extends JpaRepository<ProductWidth, Long>{
	
	@EntityGraph(attributePaths = {"productLengths"})
	@Query("select distinct w from ProductWidth w order by w.id asc")
	List<ProductWidth> findAllWidthWithLength(); 
	
	@EntityGraph(attributePaths = {"product", "productLengths"})
	List<ProductWidth> findByProduct_IdOrderByIdAsc(Long productId);
}
