package com.dev.main.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {

	private Long id;
	
	@NotBlank(message = "Category is required")
	private String categoryName;
	
	public CategoryDto() {
		
	}

	public CategoryDto(Long id, @NotBlank(message = "category is required") String categoryName) {
		super();
		this.id = id;
		this.categoryName = categoryName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
