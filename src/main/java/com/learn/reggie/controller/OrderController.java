package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.OrderDto;
import com.learn.reggie.entity.Orders;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.QueryPageParam;
import com.learn.reggie.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders orders){
        String info = orderService.submit(orders);
        return R.success(info);
    }

    @GetMapping("userPage")
    public R<Page<OrderDto>> userPage(PageParam pageParam){
        Page<OrderDto> page = orderService.userPage(pageParam);
        return R.success(page);
    }

    @GetMapping("page")
    public R<Page<Orders>> page(QueryPageParam queryPageParam){
        if (queryPageParam.getNumber() == null && queryPageParam.getBeginTime() == null){
            PageParam pageParam = new PageParam();
            BeanUtils.copyProperties(queryPageParam,pageParam);
            return page(pageParam);
        }
        Page<Orders> page = orderService.page(queryPageParam);
        return R.success(page);
    }

    /***
     * 因为缓存的原因，带查询条件时需要用一个新的方法来接收，实现带查询条件时缓存的清除
     */
    public R<Page<Orders>> page(PageParam pageParam){
        Page<Orders> page = orderService.page(pageParam);
        return R.success(page);
    }

    @PutMapping
    public R<String> changeStatus(@RequestBody Orders orders){
        String info = orderService.changeStatus(orders);
        return R.success(info);
    }
}
