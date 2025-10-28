package com.dev.main.dto;

import java.util.List;

import com.dev.main.model.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductDto {
	
	private Long id;
	
	private Category category;
	
	private List<String> imageNames;
	
	@NotBlank(message = "Title is required")
	@Size(message = "Title must be less than 150",max = 150)
	private String title;
	
	@Size(message = "Description must be less than 255",max = 255)
	private String description;
	
	@NotBlank(message = "Material is required")
	private String material;
	
	@NotBlank(message = "Theme is required")
	@Size(message = "Theme must be less than 100",max=100)
	private String theme;
	
	@NotBlank(message = "Occasion is required")
	@Size(message = "Occasion must be less than 100",max=100)
	private String occasion;
	
	public ProductDto() {
		
	}

	public ProductDto(Long id, Category category, List<String> imageNames,
			@NotBlank(message = "Title is required") @Size(message = "Title must be less than 150", max = 150) String title,
			@Size(message = "Description must be less than 255", max = 255) String description,
			@NotBlank(message = "Material is required") String material,
			@NotBlank(message = "Theme is required") @Size(message = "Theme must be less than 100", max = 100) String theme,
			@NotBlank(message = "Occasion is required") @Size(message = "Occasion must be less than 100", max = 100) String occasion) {
		super();
		this.id = id;
		this.category = category;
		this.imageNames = imageNames;
		this.title = title;
		this.description = description;
		this.material = material;
		this.theme = theme;
		this.occasion = occasion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<String> getImageNames() {
		return imageNames;
	}

	public void setImageNames(List<String> imageNames) {
		this.imageNames = imageNames;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getOccasion() {
		return occasion;
	}

	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}
}


