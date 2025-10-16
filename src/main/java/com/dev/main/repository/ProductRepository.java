package com.dev.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.main.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
