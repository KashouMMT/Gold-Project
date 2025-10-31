package com.dev.main.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dev.main.model.ProductLength;

@Repository
public interface ProductLengthRepository extends JpaRepository<ProductLength, Long>{
	@Query("""
            select min(l.price) from ProductLength l join l.productWidth w
            where w.product.id = :productId
    """)
    BigDecimal findMinPriceByProductId(@Param("productId") Long productId);

    @Query("""
        select max(l.price) from ProductLength l join l.productWidth w
        where w.product.id = :productId
    """)
    BigDecimal findMaxPriceByProductId(@Param("productId") Long productId);

    boolean existsByProductWidthIdAndLength(Long productWidthId, BigDecimal length);

    Optional<ProductLength> findByProductWidthIdAndLength(Long productWidthId, BigDecimal length);
}
