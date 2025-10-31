package com.dev.main.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "cart_items",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_cart_cart_items",
		columnNames = {"cart_id","product_id","product_width_id","product_length_id"}
	)
)
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Positive @Min(1) @Digits(integer = 8, fraction = 0)
	@Column(nullable = false)
	private int quantity;
	
	@NotNull @Digits(integer=8, fraction=2)
	@PositiveOrZero
	@Column(name= "item_price",nullable=false, precision=10, scale=2)
	private BigDecimal itemPrice;
	
	@NotNull
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Version
	private Long version;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cart_id", nullable = false,
		foreignKey = @ForeignKey(name="fk_cart_item_cart"))
	@JsonBackReference
	private Cart cart;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_cart_item_product_id"),nullable = false)
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_width_id", foreignKey = @ForeignKey(name ="fk_cart_item_product_width_id"), nullable = false)
	private ProductWidth productWidth;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_length_id", foreignKey = @ForeignKey(name = "fk_cart_item_product_length_id"), nullable = false)
	private ProductLength productLength;

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public CartItem() {
		
	}

	public CartItem(Long id, @NotNull @Digits(integer = 8, fraction = 0) int quantity,
			@NotNull @Digits(integer = 8, fraction = 2) BigDecimal itemPrice, @NotNull LocalDateTime createdAt,
			LocalDateTime updatedAt, Long version, Cart cart, Product product, ProductWidth productWidth,
			ProductLength productLength) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.itemPrice = itemPrice;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.version = version;
		this.cart = cart;
		this.product = product;
		this.productWidth = productWidth;
		this.productLength = productLength;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductWidth getProductWidth() {
		return productWidth;
	}

	public void setProductWidth(ProductWidth productWidth) {
		this.productWidth = productWidth;
	}

	public ProductLength getProductLength() {
		return productLength;
	}

	public void setProductLength(ProductLength productLength) {
		this.productLength = productLength;
	}
}
