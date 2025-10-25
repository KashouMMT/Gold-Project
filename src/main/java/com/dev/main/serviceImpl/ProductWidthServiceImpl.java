package com.dev.main.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.main.dto.ProductWidthDto;
import com.dev.main.model.ProductWidth;
import com.dev.main.repository.ProductWidthRepository;
import com.dev.main.service.ProductWidthService;

@Service
public class ProductWidthServiceImpl implements ProductWidthService{

	private final ProductWidthRepository productWidthRepo;
	
	public ProductWidthServiceImpl(ProductWidthRepository productWidthRepo) {
		this.productWidthRepo = productWidthRepo;
	}
	
	@Override
	@Transactional
	public ProductWidth createProductWidth(ProductWidthDto productWidthDto) {
		ProductWidth productWidth = new ProductWidth();
		productWidth.setProduct(productWidthDto.getProduct());
		productWidth.setWidth(productWidthDto.getWidth());
		productWidthRepo.save(productWidth);
		return productWidth;
	}

	@Override
	public ProductWidth getProductWidthById(Long id) {
		return productWidthRepo.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductWidth> getProductWidthByProductId(Long id) {
		return productWidthRepo.findByProduct_IdOrderByIdAsc(id);
	}

	@Override
	public void editProductWidth(Long id,ProductWidthDto productWidthDto) {
		ProductWidth productWidth = getProductWidthById(id);
		productWidth.setWidth(productWidthDto.getWidth());
		productWidthRepo.save(productWidth);
	}
}
