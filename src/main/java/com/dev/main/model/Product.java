package com.dev.main.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false, length = 150)
	private String title;
	
	@Size(max = 255)
	@Column(length = 255)
	private String description;
	
	@NotBlank
	@Column(nullable = false, length = 100)
	private String material;
	
	@NotBlank
	@Column(nullable = false, length = 100)
	private String theme;
	
	@NotBlank
	@Column(nullable = false, length = 100)
	private String occasion;
	
	// For testing purposes
	//@NotBlank
	//@Column(name="product_image",nullable = false, length = 1000)
	@Column(nullable = true)
	private String productImage;
	
	// Many products belong to one category
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "category_id",
		nullable = false, 
		foreignKey = @ForeignKey(name = "fk_product_category")
	)
	@JsonBackReference
	private Category category;
	
	@OneToMany(
		mappedBy = "product",
		cascade = {CascadeType.PERSIST,CascadeType.MERGE},
		orphanRemoval = false
	)
	@OrderBy("width ASC")
	@JsonManagedReference
	private List<ProductWidth> productWidths = new ArrayList<>();
	
	public Product(Long id, @NotBlank String title, @Size(max = 255) String description, @NotBlank String material,
			@NotBlank String theme, @NotBlank String occasion, @NotBlank String productImage, Category category,
			List<ProductWidth> productWidths) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.material = material;
		this.theme = theme;
		this.occasion = occasion;
		this.productImage = productImage;
		this.category = category;
		this.productWidths = productWidths;
	}

	public Product() {
		
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

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<ProductWidth> getProductWidths() {
		return productWidths;
	}

	public void setProductWidths(List<ProductWidth> productWidths) {
		this.productWidths = productWidths;
	}
}
