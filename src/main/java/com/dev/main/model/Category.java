package com.dev.main.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
	name = "categories",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_categories_name",
		columnNames = "category_name"
	)
)
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(name="category_name",nullable = false, unique = true, length = 100)
	private String categoryName;
	
	@OneToMany(
		mappedBy = "category",
		cascade = {CascadeType.PERSIST,CascadeType.MERGE},
		orphanRemoval = false
	)
	@JsonManagedReference
	List<Product> products = new ArrayList<>();

	public Category() {
		
	}

	public Category(Long id, @NotBlank String categoryName, List<Product> products) {
		super();
		this.id = id;
		this.categoryName = categoryName;
		this.products = products;
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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
