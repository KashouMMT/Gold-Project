package com.dev.main.service;

import com.dev.main.dto.ProductLengthDto;
import com.dev.main.model.ProductLength;

public interface ProductLengthService {
	ProductLength createProductLength(ProductLengthDto productLengthDto);
	ProductLength getProductLengthById(Long id);
	void editProductLength(Long id,ProductLengthDto productLengthDto);
}
