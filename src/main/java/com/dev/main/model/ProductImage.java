package com.dev.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "product_images",
	uniqueConstraints = @UniqueConstraint(columnNames = {"product_id","sort_order"})
)
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String filename;

	@Column(name = "alt_text", length = 255)
	private String altText;

	@Column(name = "sort_order", nullable = false)
	private int sortOrder = 0;

	@Column(name = "is_primary", nullable = false)
	private boolean primaryImage = false;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false,
              foreignKey = @ForeignKey(name = "fk_product_image_product"))
	private Product product;

	public ProductImage() {
		
	}
	
	public ProductImage(Long id, String filename, String altText, int sortOrder, boolean primaryImage,
			Product product) {
		super();
		this.id = id;
		this.filename = filename;
		this.altText = altText;
		this.sortOrder = sortOrder;
		this.primaryImage = primaryImage;
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isPrimaryImage() {
		return primaryImage;
	}

	public void setPrimaryImage(boolean primaryImage) {
		this.primaryImage = primaryImage;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
