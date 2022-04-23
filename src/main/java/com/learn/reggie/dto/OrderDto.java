package com.learn.reggie.dto;

import com.learn.reggie.entity.OrderDetail;
import com.learn.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private Integer sumNum;

    private List<OrderDetail> orderDetails;
}
