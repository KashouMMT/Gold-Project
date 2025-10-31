package com.dev.main.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product_lengths")
public class ProductLength {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Digits(integer = 5, fraction = 2)
	private BigDecimal length;
	
	@NotNull @Digits(integer=8, fraction=2)
	@Column(nullable=false, precision=10, scale=2)
	private BigDecimal price;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_width_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_width_length"))
	@JsonBackReference
	private ProductWidth productWidth;
	
	@OneToMany(mappedBy = "productLength",orphanRemoval = true)
	private List<CartItem> cartItems = new ArrayList<>();
	
	public ProductLength() {
		
	}

	public ProductLength(Long id, @NotNull @Digits(integer = 5, fraction = 2) BigDecimal length,
			@NotNull @Digits(integer = 8, fraction = 2) BigDecimal price, ProductWidth productWidth,
			List<CartItem> cartItems) {
		super();
		this.id = id;
		this.length = length;
		this.price = price;
		this.productWidth = productWidth;
		this.cartItems = cartItems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public ProductWidth getProductWidth() {
		return productWidth;
	}

	public void setProductWidth(ProductWidth productWidth) {
		this.productWidth = productWidth;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

}
