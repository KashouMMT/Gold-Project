package com.dev.main.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.main.components.StorageProperties;

@Service
public class FileStorageService {
	
	private static final Set<String> ALLOWED = Set.of("image/png", "image/jpeg", "image/webp");
	
	private final Path root;
	
    public FileStorageService(StorageProperties props) throws IOException {
        this.root = props.getPath();
        Files.createDirectories(root);
    }
    
    public String save(MultipartFile file) {
    	if(file == null || file.isEmpty()) return null;
    	
    	String contentType = Optional.ofNullable(file.getContentType()).orElse("");
        if (!ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported file type.");
        }
        
	    String ext = switch (contentType) {
	        case "image/png" -> ".png";
	        case "image/jpeg" -> ".jpg";
	        case "image/webp" -> ".webp";
	        default -> "";
	    };
	    
	    String safeBase = UUID.randomUUID().toString();
	    String fileName = safeBase + ext;
	    Path target = root.resolve(fileName).normalize();
	    
	    try(InputStream in = file.getInputStream()) {
	    	Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
	    } catch(IOException e) {
	    	throw new UncheckedIOException(e);
	    }
	    return fileName;
    }
    
    public void deleteIfExists(String storedName) {
        if (storedName == null || storedName.isBlank()) return;
        try { Files.deleteIfExists(root.resolve(storedName)); } catch (IOException ignored) {}
    }

    public Path getPath(String storedName) { return root.resolve(storedName); }
    public Path getRoot() { return root; }
}
