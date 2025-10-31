package com.dev.main.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.main.model.Cart;
import com.dev.main.model.CartItem;
import com.dev.main.model.Product;
import com.dev.main.model.ProductLength;
import com.dev.main.model.ProductWidth;
import com.dev.main.repository.CartItemRepository;
import com.dev.main.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService{

	private final CartItemRepository cartItemRepo;

	public CartItemServiceImpl(CartItemRepository cartItemRepo) {
		super();
		this.cartItemRepo = cartItemRepo;
	}

	@Override
	@Transactional
	public CartItem createCartItem(Cart cart,Product product,ProductWidth productWidth,ProductLength productLength,int quantity) {
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setCreatedAt(LocalDateTime.now());
		cartItem.setQuantity(quantity);
		cartItem.setItemPrice(productLength.getPrice().multiply(BigDecimal.valueOf(quantity)));
		cartItem.setProduct(product);
		cartItem.setProductWidth(productWidth);
		cartItem.setProductLength(productLength);
		return cartItemRepo.save(cartItem);
	}
}
