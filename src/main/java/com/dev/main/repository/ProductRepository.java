package com.dev.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dev.main.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
	@EntityGraph(attributePaths = {"category", "productImages"})
	@Query("select p from Product p order by p.id asc")
	List<Product> findAllWithCategoryAndImages();
	
	@EntityGraph(attributePaths = {
	        "productWidths",
	        "productWidths.productLengths"
	})
	Optional<Product> findById(Long id);
	
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findWithCategoryById(Long id);
   
    Long countByCategoryId(Long id);
}
