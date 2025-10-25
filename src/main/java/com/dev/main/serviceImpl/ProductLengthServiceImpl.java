package com.dev.main.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.main.dto.ProductLengthDto;
import com.dev.main.model.ProductLength;
import com.dev.main.repository.ProductLengthRepository;
import com.dev.main.service.ProductLengthService;

@Service
public class ProductLengthServiceImpl implements ProductLengthService{

	private final ProductLengthRepository productLengthRepo;
	
	public ProductLengthServiceImpl(ProductLengthRepository productLengthRepo) {
		this.productLengthRepo = productLengthRepo;
	}
	
	@Override
	@Transactional
	public ProductLength createProductLength(ProductLengthDto productLengthDto) {
		ProductLength productLength = new ProductLength();
		productLength.setLength(productLengthDto.getLength());
		productLength.setPrice(productLengthDto.getPrice());
		productLength.setProductWidth(productLengthDto.getWidth());
		productLengthRepo.save(productLength);
		return productLength;
	}

	@Override
	public ProductLength getProductLengthById(Long id) {
		return productLengthRepo.findById(id).orElse(null);
	}

	@Override
	public void editProductLength(Long id,ProductLengthDto productLengthDto) {
		ProductLength productLength = getProductLengthById(id);
	    if (productLengthDto.getLength() != null) {
	        productLength.setLength(productLengthDto.getLength());
	    }

	    if (productLengthDto.getPrice() != null) {
	        productLength.setPrice(productLengthDto.getPrice());
	    }
		productLengthRepo.save(productLength);
	}
}
