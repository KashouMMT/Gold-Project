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

import com.dev.main.dto.CategoryDto;
import com.dev.main.dto.ProductDto;
import com.dev.main.dto.ProductLengthDto;
import com.dev.main.dto.ProductWidthDto;
import com.dev.main.dto.VariationDto;
import com.dev.main.model.Category;
import com.dev.main.model.Product;
import com.dev.main.model.ProductWidth;
import com.dev.main.repository.CategoryRepository;
import com.dev.main.repository.ProductRepository;
import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductLengthService;
import com.dev.main.service.ProductService;
import com.dev.main.service.ProductWidthService;

@Service
public class ProductServiceImpl implements ProductService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private final ProductRepository productRepo;
	
	private final CategoryRepository categoryRepo;
	
	private final CategoryService categoryService;
	
	private final ProductWidthService productWidthService;
	
	private final ProductLengthService productLengthService;
	
	public ProductServiceImpl(ProductRepository productRepo,CategoryRepository categoryRepo,CategoryService categoryService,
			ProductWidthService productWidthService,ProductLengthService productLengthService) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
		this.categoryService = categoryService;
		this.productWidthService = productWidthService;
		this.productLengthService = productLengthService;
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
	public List<Product> getAllForTable() {
		return productRepo.findAllProductsWithCategories();
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
	public Product createProduct(ProductDto productDto) {
//		private Long id;
//		private String title;
//		private String description;
//		private String material;
//		private String theme;
//		private String occasion;
//		private String productImage;
//		private Category category;
//		private List<ProductWidth> productWidths = new ArrayList<>();
		Product product = new Product();
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setMaterial(productDto.getMaterial());
		product.setTheme(productDto.getTheme());
		product.setOccasion(productDto.getOccasion());
		product.setCategory(productDto.getCategory());
		return productRepo.save(product);
	}

	@Override
	@Transactional
	public void createForTable(ProductDto productDto, CategoryDto categoryDto, VariationDto variationDto) {
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
		Product product = createProduct(productDto);
	    BigDecimal[]   width  = variationDto.getWidth();
	    BigDecimal[][] length = variationDto.getLength();
	    BigDecimal[][] price  = variationDto.getPrice();
	    for(int i = 0; i < width.length; i++) {
	    	ProductWidthDto widthDto = new ProductWidthDto();
	    	widthDto.setWidth(width[i]);
	    	widthDto.setProduct(product);
	    	ProductWidth productWidth = productWidthService.createProductWidth(widthDto);
	    	for(int j = 0; j < length[i].length; j++) {
	    		ProductLengthDto lengthDto = new ProductLengthDto();
	    		lengthDto.setLength(length[i][j]);
	    		lengthDto.setPrice(price[i][j]);
	    		lengthDto.setWidth(productWidth);
	    		productLengthService.createProductLength(lengthDto);
	    	}
	    }
	}

	@Override
	public Product getProductById(Long id) {
		return productRepo.findById(id).orElse(null);
	}

	@Override
	public List<Product> getAllProductsWithCategories() {
		return productRepo.findAllProductsWithCategories();
	}

	@Override
	public Product getProductWithCategoryById(Long id) {
		return productRepo.findWithCategoryById(id).orElse(null);
	}

	@Override
	@Transactional
	public void editProductWithCategory(Long id, ProductDto productDto, CategoryDto categoryDto) {
		Category category = categoryRepo.findByCategoryName(categoryDto.getCategoryName()).orElse(null);
		if(Objects.isNull(category)) {
			category = new Category();
			category.setCategoryName(categoryDto.getCategoryName());
			categoryRepo.save(category);
		} 
		Product product = getProductById(id);
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setMaterial(productDto.getMaterial());
		product.setTheme(productDto.getTheme());
		product.setOccasion(productDto.getOccasion());
		product.setCategory(category);
		productRepo.save(product);
	}
}
