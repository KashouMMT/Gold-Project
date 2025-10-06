package com.dev.main.service;

import java.util.List;

import com.dev.main.model.Product;

public interface ProductService {
	List<Product> getAllProducts();
	void createProduct();
}
