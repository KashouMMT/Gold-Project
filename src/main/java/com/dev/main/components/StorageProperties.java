package com.dev.main.components;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {
	/**
	 * Absolute or relative directory for uploads, e.g.
	 * - local: ./uploads
	 * - railway: /app/data/uploads (volume mounted here)
	 */
	private Path path;
	
	public Path getPath() {return path;}
	public void setPath(String path) {this.path = Path.of(path);}
}
