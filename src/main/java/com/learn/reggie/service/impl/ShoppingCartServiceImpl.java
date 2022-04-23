package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.ShoppingCart;
import com.learn.reggie.mapper.ShoppingCartMapper;
import com.learn.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, CommonThreadLocal.getUser());
        lqw.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(lqw);
        return R.success(shoppingCartList);
    }

    @Override
    public R<ShoppingCart> add(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(CommonThreadLocal.getUser());
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if (dishId != null){
            lqw.eq(ShoppingCart::getDishId, dishId);
        }else {
            lqw.eq(ShoppingCart::getSetmealId, setmealId);
        }

        ShoppingCart selectOne = shoppingCartMapper.selectOne(lqw);
        if (selectOne != null){
            selectOne.setNumber(selectOne.getNumber() + 1);
            shoppingCartMapper.updateById(selectOne);
        }else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            selectOne = shoppingCart;
        }

        return R.success(selectOne);
    }

    @Override
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, CommonThreadLocal.getUser());
        shoppingCartMapper.delete(lqw);
        return R.success("清空成功");
    }

    @Override
    public R<ShoppingCart> sub(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(CommonThreadLocal.getUser());
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if (dishId != null){
            lqw.eq(ShoppingCart::getDishId, dishId);
        }else {
            lqw.eq(ShoppingCart::getSetmealId, setmealId);
        }

        ShoppingCart selectOne = shoppingCartMapper.selectOne(lqw);
        if (selectOne != null && selectOne.getNumber() > 1){
            selectOne.setNumber(selectOne.getNumber() - 1);
            shoppingCartMapper.updateById(selectOne);
        }

        return R.success(selectOne);
    }


}
