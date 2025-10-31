package com.dev.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.main.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

}
