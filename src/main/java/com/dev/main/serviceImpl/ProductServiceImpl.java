package com.dev.main.serviceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dev.main.dto.CategoryDto;
import com.dev.main.dto.ProductDto;
import com.dev.main.dto.ProductLengthDto;
import com.dev.main.dto.ProductWidthDto;
import com.dev.main.dto.VariationDto;
import com.dev.main.model.Category;
import com.dev.main.model.Product;
import com.dev.main.model.ProductImage;
import com.dev.main.model.ProductWidth;
import com.dev.main.repository.CategoryRepository;
import com.dev.main.repository.ProductImageRepository;
import com.dev.main.repository.ProductLengthRepository;
import com.dev.main.repository.ProductRepository;
import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductImageService;
import com.dev.main.service.ProductLengthService;
import com.dev.main.service.ProductService;
import com.dev.main.service.ProductWidthService;

@Service
public class ProductServiceImpl implements ProductService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;
	private final ProductImageRepository productImageRepo;
	private final ProductLengthRepository productLengthRepo;
	private final CategoryService categoryService;
	private final ProductWidthService productWidthService;
	private final ProductLengthService productLengthService;
	private final FileStorageService fileStorageService;
	private final ProductImageService productImageService;

	public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo,
			ProductImageRepository productImageRepo, ProductLengthRepository productLengthRepo,
			CategoryService categoryService, ProductWidthService productWidthService,
			ProductLengthService productLengthService, FileStorageService fileStorageService,
			ProductImageService productImageService) {
		super();
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
		this.productImageRepo = productImageRepo;
		this.productLengthRepo = productLengthRepo;
		this.categoryService = categoryService;
		this.productWidthService = productWidthService;
		this.productLengthService = productLengthService;
		this.fileStorageService = fileStorageService;
		this.productImageService = productImageService;
	}

	@Override
	public List<Product> getAllProducts() {
		logger.info("Fetching all products from the database.");
		List<Product> products = productRepo.findAll();
		logger.debug("Total products retrieved: {}", products.size());
		return products;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Product> getAllWithCategoryAndImage() {
		return productRepo.findAllWithCategoryAndImages();
	}

	@Override
	public List<String> getAllMaterials() {
		return productRepo.findAll().stream()
				.map(Product::getMaterial)
				.filter(Objects::nonNull)
				.distinct().collect(Collectors.toList());
	}

	@Override
	public List<String> getAllOccasion() {
		return productRepo.findAll().stream()
				.map(Product::getOccasion)
				.filter(Objects::nonNull)
				.distinct().collect(Collectors.toList());
	}

	@Override
	public List<String> getAllTheme() {
		return productRepo.findAll().stream()
				.map(Product::getTheme)
				.filter(Objects::nonNull)
				.distinct().collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public Product createProduct(ProductDto productDto,MultipartFile[] images) {
		Product product = new Product();
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setMaterial(productDto.getMaterial());
		product.setTheme(productDto.getTheme());
		product.setOccasion(productDto.getOccasion());
		product.setCategory(productDto.getCategory());
		product = productRepo.save(product);
		for (int i = 0; i < images.length; i++) {
	        MultipartFile file = images[i];
	        if (file != null && !file.isEmpty()) {
	            String stored = fileStorageService.save(file);
	            if (stored != null) {
	                productImageService.createProductImages(stored, product, i);
	            }
	        }
	    }
		return product;
	}

	@Override
	@Transactional
	public void createForTable(ProductDto productDto, CategoryDto categoryDto, VariationDto variationDto,MultipartFile[] images) {
		Category category = categoryRepo.findByCategoryName(categoryDto.getCategoryName())
			.orElseGet(() -> {
				try {
					return categoryService.createCategory(categoryDto);
				} catch (DataIntegrityViolationException e) {
					return categoryRepo.findByCategoryName(categoryDto.getCategoryName())
						.orElseThrow(() -> new IllegalStateException("Category just created but not found"));
				}
		});
		
		productDto.setCategory(category);
		Product product = createProduct(productDto,images);
	    BigDecimal[]   width  = variationDto.getWidth();
	    BigDecimal[][] length = variationDto.getLength();
	    BigDecimal[][] price  = variationDto.getPrice();
	    for(int i = 0; i < width.length; i++) {
	    	ProductWidthDto widthDto = new ProductWidthDto();
	    	widthDto.setWidth(width[i]);
	    	widthDto.setProduct(product);
	    	ProductWidth productWidth = productWidthService.createProductWidth(widthDto);
	    	for (int j = 0; j < length[i].length; j++) {
	            BigDecimal L = length[i][j];
	            BigDecimal P = price[i][j];

	            productLengthRepo.findByProductWidthIdAndLength(productWidth.getId(), L)
	                .ifPresentOrElse(existing -> {
	                    existing.setPrice(P);
	                    productLengthRepo.save(existing);
	                }, () -> {
	                    ProductLengthDto lengthDto = new ProductLengthDto();
	                    lengthDto.setLength(L);
	                    lengthDto.setPrice(P);
	                    lengthDto.setWidth(productWidth);
	                    productLengthService.createProductLength(lengthDto);
	                });
	        }
	    }
	    BigDecimal min = productLengthRepo.findMinPriceByProductId(product.getId());
	    BigDecimal max = productLengthRepo.findMaxPriceByProductId(product.getId());
	    product.setMinPrice(min);
	    product.setMaxPrice(max);
	    
	    productRepo.save(product);
	}

	@Override
	public Product getProductById(Long id) {
		return productRepo.findById(id).orElse(null);
	}

	@Override
	public Product getProductWithCategoryById(Long id) {
		return productRepo.findWithCategoryById(id).orElse(null);
	}

	@Override
	@Transactional
	public void editProductWithCategory(Long id, ProductDto productDto, CategoryDto categoryDto,MultipartFile[] images) {
		Category category = categoryRepo.findByCategoryName(categoryDto.getCategoryName()).orElse(null);
		if(Objects.isNull(category)) {
			category = new Category();
			category.setCategoryName(categoryDto.getCategoryName());
			categoryRepo.save(category);
		} 
		Product product = getProductById(id);

	    // 1) Remove old files + rows
	    List<ProductImage> old = productImageRepo.findByProductIdOrderBySortOrderAsc(id);
	    for (ProductImage pi : old) fileStorageService.deleteIfExists(pi.getFilename());
	    product.getProductImages().clear(); 
	    productImageRepo.deleteByProductId(id);

	    // 2) Add new images
	    int order = 0;
	    if (images != null) {
	        for (MultipartFile f : images) {
	            if (f == null || f.isEmpty()) continue;
	            String stored = fileStorageService.save(f);
	            ProductImage pi = new ProductImage();
	            pi.setFilename(stored);
	            pi.setAltText(stored);
	            pi.setSortOrder(order);
	            pi.setPrimaryImage(order == 0);
	            pi.setProduct(product);
	            product.getProductImages().add(pi);
	            order++;
	        }
	    }
	    
	    BigDecimal min = productLengthRepo.findMinPriceByProductId(product.getId());
	    BigDecimal max = productLengthRepo.findMaxPriceByProductId(product.getId());
	    product.setMinPrice(min);
	    product.setMaxPrice(max);
	    
	    productRepo.save(product);
	}

	@Override
	public void deleteProduct(Long id) {
		List<ProductImage> images = productImageRepo.findByProductIdOrderBySortOrderAsc(id);
		for(ProductImage pi : images) {
			fileStorageService.deleteIfExists(pi.getFilename());
		}
		productRepo.deleteById(id);
	}

	@Override
	public void updateMinMaxPrice(Long id) {
		Product product = productRepo.findById(id).orElse(null);
	    BigDecimal min = productLengthRepo.findMinPriceByProductId(id);
	    BigDecimal max = productLengthRepo.findMaxPriceByProductId(id);
	    product.setMinPrice(min);
	    product.setMaxPrice(max);
	    productRepo.save(product);
	}
}
