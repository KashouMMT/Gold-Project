package com.dev.main.dto;

import java.math.BigDecimal;

import com.dev.main.model.Product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDto {
	
	private Long id;
	
	@NotBlank
	@Column(nullable = false, length = 150)
	private String title;
	
	@Column(length = 255)
	private String summary;
	
	@NotNull
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal price;
	
	@NotNull
	private Long category;

	public ProductDto(Long id, @NotBlank String title, String summary, @NotNull BigDecimal price,
			@NotNull Long category) {
		super();
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.price = price;
		this.category = category;
	}

	public ProductDto(Product product) {
		this.id = product.getId();
		this.title = product.getTitle();
		this.summary = product.getSummary();
		this.price = product.getPrice();
		this.category = product.getCategory().getId();
	}
	
	public ProductDto() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}
}
