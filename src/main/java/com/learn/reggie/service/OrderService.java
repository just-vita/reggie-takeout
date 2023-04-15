package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.dto.OrderDto;
import com.learn.reggie.entity.Orders;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.QueryPageParam;

public interface OrderService {

    String submit(Orders orders);

    Page<OrderDto> userPage(PageParam pageParam);

    Page<Orders> page(PageParam pageParam);

    Page<Orders> page(QueryPageParam pageParam);

    String changeStatus(Orders orders);


}