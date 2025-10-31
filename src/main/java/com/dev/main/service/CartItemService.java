package com.dev.main.service;

import com.dev.main.model.Cart;
import com.dev.main.model.CartItem;
import com.dev.main.model.Product;
import com.dev.main.model.ProductLength;
import com.dev.main.model.ProductWidth;

public interface CartItemService {
	CartItem createCartItem(Cart cart,Product product,ProductWidth productWidth,ProductLength productLength,int quantity);
}
