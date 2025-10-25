package com.dev.main.model;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_secret")
public class AppSecret {
	
	@Id
	@Column(length = 100)
	private String name;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(nullable = false, unique = true)
	private String value;
	
	@Column(name = "updated_at",nullable = false)
	private LocalDateTime updatedAt = LocalDateTime.now();
	
	public AppSecret() {
		
	}
	
    @PreUpdate
    public void touch() { 
    	this.updatedAt = LocalDateTime.now(); 
    }

	public AppSecret(String name, String value, LocalDateTime updatedAt) {
		super();
		this.name = name;
		this.value = value;
		this.updatedAt = updatedAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
