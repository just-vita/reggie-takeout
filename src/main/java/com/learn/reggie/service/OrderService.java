package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.OrderDto;
import com.learn.reggie.entity.Orders;
import com.learn.reggie.entity.PageParam;

public interface OrderService {

    R<String> submit(Orders orders);

    R<Page<OrderDto>> userPage(PageParam pageParam);

    R<Page<Orders>> page(PageParam pageParam);

    R<String> changeStatus(Orders orders);


}