package com.learn.reggie.controller;

import com.learn.reggie.common.R;
import com.learn.reggie.entity.ShoppingCart;
import com.learn.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("list")
    public R<List<ShoppingCart>> list(){
        return shoppingCartService.list();
    }

    @PostMapping("add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.add(shoppingCart);
    }

    @PostMapping("sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.sub(shoppingCart);
    }

    @DeleteMapping("clean")
    public R<String> clean(){
        return shoppingCartService.clean();
    }
}
