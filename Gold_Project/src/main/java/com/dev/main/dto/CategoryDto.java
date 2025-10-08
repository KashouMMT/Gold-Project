package com.dev.main.dto;

import com.dev.main.model.Category;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
	
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true, length = 100)
	private String category_name;
	
	@Column(length = 255)
	private String description;
	
	public CategoryDto(Long id, @NotBlank String category_name, String description) {
		super();
		this.id = id;
		this.category_name = category_name;
		this.description = description;
	}

	public CategoryDto(Category category) {
		this.id = category.getId();
		this.category_name = category.getCategory_name();
		this.description = category.getDescription();
	}
	
	public CategoryDto() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
