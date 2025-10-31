package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.ProductWidthDto;
import com.dev.main.model.ProductWidth;

public interface ProductWidthService {
	
	List<ProductWidth> getProductWidthByProductId(Long id);
	
	ProductWidth createProductWidth(ProductWidthDto productWidthDto);
	ProductWidth getProductWidthById(Long id);

	void editProductWidth(Long id,ProductWidthDto productWidthDto);
}
