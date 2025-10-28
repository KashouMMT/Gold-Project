package com.dev.main.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dev.main.serviceImpl.FileStorageService;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private final FileStorageService storage;

  	public WebConfig(FileStorageService storage) {
	  this.storage = storage;
  	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		String location = storage.getRoot().toAbsolutePath().toUri().toString();
		registry.addResourceHandler("/files/**")
				.addResourceLocations(location)
				.setCachePeriod(3600);
	}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.ALL);
	}
}
