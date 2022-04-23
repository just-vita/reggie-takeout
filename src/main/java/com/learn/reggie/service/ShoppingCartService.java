package com.learn.reggie.service;

import com.learn.reggie.common.R;
import com.learn.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    R<List<ShoppingCart>> list();

    R<ShoppingCart> add(ShoppingCart shoppingCart);

    R<String> clean();

    R<ShoppingCart> sub(ShoppingCart shoppingCart);
}
