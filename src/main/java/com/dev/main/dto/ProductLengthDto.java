package com.dev.main.dto;

import java.math.BigDecimal;

import com.dev.main.model.ProductWidth;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductLengthDto {

    private Long id;
    
    private ProductWidth width;

    @NotNull(message = "Length is required")
    @Digits(integer = 5, fraction = 2, message = "Length must be a valid decimal")
    @Positive(message = "Length must be positive")
    private BigDecimal length;

    @NotNull(message = "Price is required")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid decimal")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    public ProductLengthDto() {
    	
    }

	public ProductLengthDto(Long id, ProductWidth width,
			@NotNull(message = "Length is required") @Digits(integer = 5, fraction = 2, message = "Length must be a valid decimal") @Positive(message = "Length must be positive") BigDecimal length,
			@NotNull(message = "Price is required") @Digits(integer = 8, fraction = 2, message = "Price must be a valid decimal") @Positive(message = "Price must be positive") BigDecimal price) {
		super();
		this.id = id;
		this.width = width;
		this.length = length;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductWidth getWidth() {
		return width;
	}

	public void setWidth(ProductWidth width) {
		this.width = width;
	}

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}

