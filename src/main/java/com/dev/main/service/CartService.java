package com.dev.main.service;

import com.dev.main.model.Cart;
import com.dev.main.model.User;

public interface CartService {
	void createCartAndCartItemIfNotExist(User user,Long productId,Long widthId,Long lengthId,int quantity);
	Cart createCart(User user);
	Cart getCartByUserId(Long id);
	Cart getCartViewByUserId(Long id);
}
