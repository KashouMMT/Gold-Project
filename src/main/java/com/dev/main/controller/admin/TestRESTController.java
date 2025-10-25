//package com.dev.main.controller.admin;
//
//import java.util.List;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dev.main.dto.CategoryDto;
//import com.dev.main.dto.ProductDto;
//import com.dev.main.model.Product;
//import com.dev.main.service.ProductService;
//
//@RestController
//public class TestRESTController {
//    private final ProductService service;
//    
//    public TestRESTController(ProductService service) {
//        this.service = service;
//    }
//    
//    @PostMapping("/products/add-product/step-1")
//    public ResponseEntity<ProductDto> listAllProducts(
//    	@ModelAttribute("productDto")ProductDto productDto,
//    	@ModelAttribute("categoryDto")CategoryDto categoryDto) {
//    	
//        return ResponseEntity.ok(productDto);
//    }
//}
//
//
