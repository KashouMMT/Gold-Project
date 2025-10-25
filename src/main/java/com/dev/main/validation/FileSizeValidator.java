package com.dev.main.validation;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile>{

	private long maxSizse;
	
	@Override
	public void initialize(FileSize constraintAnnotation) {
		this.maxSizse = constraintAnnotation.max();
	}
	
	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if(file == null || file.isEmpty()) return false;
		return file.getSize() <= maxSizse;
	}
}
