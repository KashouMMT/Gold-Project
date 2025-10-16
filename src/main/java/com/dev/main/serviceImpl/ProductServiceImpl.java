package com.dev.main.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dev.main.model.Product;
import com.dev.main.repository.ProductRepository;
import com.dev.main.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private ProductRepository productRepo;
	
	public ProductServiceImpl(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}

	@Override
	public List<Product> getAllProducts() {
		logger.info("Fetching all products from the database.");
		List<Product> products = productRepo.findAll();
		logger.debug("Total products retrieved: {}", products.size());
		return products;
	}

	@Override
	public void createProduct() {
		// TODO Auto-generated method stub
		
	}
	
	
}
