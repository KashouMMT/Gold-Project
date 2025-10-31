package com.dev.main.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jakarta.persistence.Version;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "order_items")
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull @Min(1)
	@Column(nullable = false,name = "quantity",precision = 0,scale = 0)
	private int quantity;
	
	@NotNull @Digits(integer=8, fraction=2)
	@Column(name= "order_item_price",nullable=false, precision=10, scale=2)
	private BigDecimal orderItemPrice;
	
	@NotNull
	@Column(name = "created_at",nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Version
	private Long version;
	
	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn(
		name = "product_id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_product_order_item"))
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn(
		name ="order_id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_order_order_item"))
	private Order order;
	
	@PrePersist
	public void prePersit() {
		createdAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	public OrderItem() {
		
	}

	public OrderItem(Long id, @NotNull @Min(1) int quantity,
			@NotNull @Digits(integer = 8, fraction = 2) BigDecimal orderItemPrice, @NotNull LocalDateTime createdAt,
			LocalDateTime updatedAt, Long version, Product product, Order order) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.orderItemPrice = orderItemPrice;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.version = version;
		this.product = product;
		this.order = order;
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

	public BigDecimal getOrderItemPrice() {
		return orderItemPrice;
	}

	public void setOrderItemPrice(BigDecimal orderItemPrice) {
		this.orderItemPrice = orderItemPrice;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
