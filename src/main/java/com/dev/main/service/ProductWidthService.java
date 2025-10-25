package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.ProductWidthDto;
import com.dev.main.model.ProductWidth;

public interface ProductWidthService {
	ProductWidth createProductWidth(ProductWidthDto productWidthDto);
	void editProductWidth(Long id,ProductWidthDto productWidthDto);
	ProductWidth getProductWidthById(Long id);
	List<ProductWidth> getProductWidthByProductId(Long id);
}
