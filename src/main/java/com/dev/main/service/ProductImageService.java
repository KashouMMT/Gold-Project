package com.dev.main.service;

import java.util.List;

import com.dev.main.model.Product;
import com.dev.main.model.ProductImage;

public interface ProductImageService {
	void createProductImages(String imageName,Product product,int sortOrder);
	List<ProductImage> getAllProductImages();
	ProductImage getFirstProductImageByProductId(Long id);
}
