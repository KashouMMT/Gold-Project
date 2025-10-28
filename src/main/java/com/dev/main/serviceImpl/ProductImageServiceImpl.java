package com.dev.main.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.main.model.Product;
import com.dev.main.model.ProductImage;
import com.dev.main.repository.ProductImageRepository;
import com.dev.main.service.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {

	private final ProductImageRepository productImageRepo;
	
	public ProductImageServiceImpl(ProductImageRepository productImageRepo) {
		this.productImageRepo = productImageRepo;
	}
	
	@Override
	@Transactional
	public void createProductImages(String imageName,Product product,int sortOrder) {
		ProductImage productImage = new ProductImage();
		productImage.setFilename(imageName);
		productImage.setProduct(product);
		productImage.setAltText(imageName);
		productImage.setSortOrder(sortOrder);
		if(sortOrder == 0) productImage.setPrimaryImage(true); 
		else productImage.setPrimaryImage(false);
		productImageRepo.save(productImage);
	}

	@Override
	public List<ProductImage> getAllProductImages() {
		return productImageRepo.findAll();
	}
}
