package com.dev.main.dto;

import java.math.BigDecimal;

import com.dev.main.model.Product;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductWidthDto {

    private Long id;
    
    private Product product;

    @NotNull(message = "Width is required")
    @Digits(integer = 5, fraction = 2, message = "Width must be a valid decimal")
    @Positive(message = "Width must be positive")
    private BigDecimal width;

    public ProductWidthDto() {
    	
    }

	public ProductWidthDto(Long id, Product product,
			@NotNull(message = "Width is required") @Digits(integer = 5, fraction = 2, message = "Width must be a valid decimal") @Positive(message = "Width must be positive") BigDecimal width) {
		super();
		this.id = id;
		this.product = product;
		this.width = width;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}
}
