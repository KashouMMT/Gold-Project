package com.dev.main.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

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
import jakarta.validation.constraints.Digits;
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
	
	@Digits(integer=8, fraction=2)
	@Column(name = "min_price", precision=10, scale=2)
	private BigDecimal minPrice;
	
	@Digits(integer=8, fraction=2)
	@Column(name = "max_price", precision=10, scale=2)
	private BigDecimal maxPrice;
	
	// Many products belong to one category
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "category_id",
		nullable = false, 
		foreignKey = @ForeignKey(name = "fk_product_category"))
	@JsonBackReference
	private Category category;
	
	@OneToMany(
		mappedBy = "product",
		cascade = CascadeType.ALL,
		orphanRemoval = false)
	@OrderBy("width ASC")
	@JsonManagedReference
	private List<ProductWidth> productWidths = new ArrayList<>();
	
	@OneToMany(
		mappedBy = "product",
	    cascade = CascadeType.ALL,
	    fetch = FetchType.EAGER,
	    orphanRemoval = true)
	@OrderBy("sortOrder ASC, id ASC")
	private List<ProductImage> productImages = new ArrayList<>();
	
	@OneToMany(mappedBy = "product")
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@OneToMany(mappedBy = "product",orphanRemoval = true)
	private List<CartItem> cartItems = new ArrayList<>();
	
	public String getMainProductImage() {
		return productImages.getFirst().getFilename();
	}
	
	public Product() {
		
	}

	public Product(Long id, @NotBlank String title, @Size(max = 255) String description, @NotBlank String material,
			@NotBlank String theme, @NotBlank String occasion, @Digits(integer = 8, fraction = 2) BigDecimal minPrice,
			@Digits(integer = 8, fraction = 2) BigDecimal maxPrice, Category category, List<ProductWidth> productWidths,
			List<ProductImage> productImages, List<OrderItem> orderItems, List<CartItem> cartItems) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.material = material;
		this.theme = theme;
		this.occasion = occasion;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.category = category;
		this.productWidths = productWidths;
		this.productImages = productImages;
		this.orderItems = orderItems;
		this.cartItems = cartItems;
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

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
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

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

}
