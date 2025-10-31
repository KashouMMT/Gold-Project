package com.dev.main.model;

import java.math.BigDecimal;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
	name = "product_widths",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_product_width",
		columnNames = {"product_id","width"}
))
public class ProductWidth {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Digits(integer = 5, fraction = 2)
	@Column(nullable = false, precision = 7, scale = 2)
	private BigDecimal width;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="product_id",nullable = false,
		foreignKey = @ForeignKey(name = "fk_product_width_product"))
	@JsonBackReference
	private Product product;
	
	@OneToMany(
		mappedBy = "productWidth",
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	@OrderBy("length ASC")
	@JsonManagedReference
	private List<ProductLength> productLengths = new ArrayList<>();
	
	public ProductWidth() {
		
	}

	public ProductWidth(Long id, @NotNull @Digits(integer = 5, fraction = 2) BigDecimal width, Product product,
			List<ProductLength> productLengths) {
		super();
		this.id = id;
		this.width = width;
		this.product = product;
		this.productLengths = productLengths;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductLength> getProductLengths() {
		return productLengths;
	}

	public void setProductLengths(List<ProductLength> productLengths) {
		this.productLengths = productLengths;
	}
}
