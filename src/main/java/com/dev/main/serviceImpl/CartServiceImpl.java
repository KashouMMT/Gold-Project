package com.dev.main.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.main.model.Cart;
import com.dev.main.model.CartItem;
import com.dev.main.model.Product;
import com.dev.main.model.ProductLength;
import com.dev.main.model.ProductWidth;
import com.dev.main.model.User;
import com.dev.main.repository.CartRepository;
import com.dev.main.service.CartItemService;
import com.dev.main.service.CartService;
import com.dev.main.service.ProductLengthService;
import com.dev.main.service.ProductService;
import com.dev.main.service.ProductWidthService;
import com.dev.main.utils.CartStatus;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CartServiceImpl implements CartService{
	
	private final CartRepository cartRepo;
	private final ProductService productService;
	private final ProductWidthService productWidthService;
	private final ProductLengthService productLengthService;
	private final CartItemService cartItemService;

	public CartServiceImpl(CartRepository cartRepo, ProductService productService,
			ProductWidthService productWidthService, ProductLengthService productLengthService,
			CartItemService cartItemService) {
		super();
		this.cartRepo = cartRepo;
		this.productService = productService;
		this.productWidthService = productWidthService;
		this.productLengthService = productLengthService;
		this.cartItemService = cartItemService;
	}

	@Override
	@Transactional
	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setStatus(CartStatus.ACTIVE);
		cart.setCreatedAt(LocalDateTime.now());
		cart.setTotalPrice(BigDecimal.ZERO);
		return cartRepo.save(cart);
	}
	
	@Override
	@Transactional
	public void createCartAndCartItemIfNotExist(User user,Long productId,Long widthId,Long lengthId,int quantity) {
		
		Cart cart = cartRepo.findByUserId(user.getId()).orElseGet(() -> createCart(user));
	    
		Product product = productService.getProductById(productId);
		ProductWidth productWidth = productWidthService.getProductWidthById(widthId);
	    ProductLength productLength = productLengthService.getProductLengthById(lengthId);
	    
	    Optional<CartItem> existingItem = cart.getCartItems().stream()
	    	.filter(item -> item.getProductLength().getId().equals(lengthId))
	    	.findFirst();
	    
	    if(existingItem.isPresent()) {
	    	CartItem item = existingItem.get();
	        item.setQuantity(item.getQuantity() + quantity);
	        item.setItemPrice(productLength.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
	    } else {
	    	CartItem cartItem = cartItemService.createCartItem(cart,product,productWidth,productLength, quantity);
	    	cart.getCartItems().add(cartItem);
	    }    
	    
	    BigDecimal total = cart.getCartItems().stream()
            .map(CartItem::getItemPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    cart.setTotalPrice(total);
	    
		cartRepo.save(cart);
	}

	@Override
	public Cart getCartByUserId(Long id) {
		return cartRepo.findByUserId(id).orElse(null);
	}

	@Override
	public Cart getCartViewByUserId(Long id) {
		return cartRepo.findCartViewByUserId(id).orElse(null);
	}
}
