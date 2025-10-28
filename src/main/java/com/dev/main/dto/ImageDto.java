package com.dev.main.dto;

import org.springframework.web.multipart.MultipartFile;

public class ImageDto {
	
	private MultipartFile[] images;
	
	public ImageDto() {
		
	}

	public ImageDto(MultipartFile[] images) {
		super();
		this.images = images;
	}

	public MultipartFile[] getImages() {
		return images;
	}

	public void setImages(MultipartFile[] images) {
		this.images = images;
	}
}
