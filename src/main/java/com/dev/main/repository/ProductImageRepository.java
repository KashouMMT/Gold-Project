package com.dev.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dev.main.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
	
	List<ProductImage> findByProductIdOrderBySortOrderAsc(Long id);
    @Transactional
    void deleteByProductId(Long productId);
}
