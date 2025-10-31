package com.dev.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dev.main.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	
	Optional<Cart> findByUserId(Long id);

	@Query("""
		select distinct c from Cart c
		join fetch c.cartItems ci
		join fetch ci.product p 
		left join fetch ci.productWidth pw
		left join fetch ci.productLength pl
		where c.user.id = :userId
	""")
	Optional<Cart> findCartViewByUserId(@Param("userId") Long id);
}
