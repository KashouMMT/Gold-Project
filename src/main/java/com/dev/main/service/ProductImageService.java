package com.dev.main.service;

import java.util.List;

import com.dev.main.model.Product;
import com.dev.main.model.ProductImage;

public interface ProductImageService {
	List<ProductImage> getAllProductImages();
	List<ProductImage> getAllProductImagesByProductId(Long id);
	ProductImage getFirstProductImageByProductId(Long id);
	void createProductImages(String imageName,Product product,int sortOrder);
}
