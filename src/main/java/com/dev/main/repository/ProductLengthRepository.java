package com.dev.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.main.model.ProductLength;

@Repository
public interface ProductLengthRepository extends JpaRepository<ProductLength, Long>{

}
