package com.dev.main.dto;

import java.math.BigDecimal;

public class VariationDto {
    private BigDecimal[]   width;
    private BigDecimal[][] length;
    private BigDecimal[][] price;
    public VariationDto() {}
	public VariationDto(BigDecimal[] width, BigDecimal[][] length, BigDecimal[][] price) {
		super();
		this.width = width;
		this.length = length;
		this.price = price;
	}
	public BigDecimal[] getWidth() {
		return width;
	}
	public void setWidth(BigDecimal[] width) {
		this.width = width;
	}
	public BigDecimal[][] getLength() {
		return length;
	}
	public void setLength(BigDecimal[][] length) {
		this.length = length;
	}
	public BigDecimal[][] getPrice() {
		return price;
	}
	public void setPrice(BigDecimal[][] price) {
		this.price = price;
	}
}
