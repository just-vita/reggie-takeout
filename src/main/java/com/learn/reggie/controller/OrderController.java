package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.OrderDto;
import com.learn.reggie.entity.Orders;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders orders){
        return orderService.submit(orders);
    }

    @GetMapping("userPage")
    public R<Page<OrderDto>> userPage(PageParam pageParam){
        return orderService.userPage(pageParam);
    }

    @GetMapping("page")
    public R<Page<Orders>> page(PageParam pageParam){
        return orderService.page(pageParam);
    }

    @PutMapping
    public R<String> changeStatus(Orders orders){
        return orderService.changeStatus(orders);
    }
}
