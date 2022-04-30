package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.common.CustomException;
import com.learn.reggie.dto.OrderDto;
import com.learn.reggie.entity.*;
import com.learn.reggie.mapper.*;
import com.learn.reggie.service.OrderDetailService;
import com.learn.reggie.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = {"order_page"})
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public String submit(Orders orders) {
        Long userId = CommonThreadLocal.getUser();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(wrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userMapper.selectById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookMapper.selectById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }

        long orderId = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        orderMapper.insert(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartMapper.delete(wrapper);

        return "下单成功";
    }

    @Override
    public Page<OrderDto> userPage(PageParam pageParam) {
        Page<Orders> ordersPage = new Page<Orders>(pageParam.getPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Orders::getUserId, CommonThreadLocal.getUser());
        lqw.orderByDesc(Orders::getOrderTime);
        orderMapper.selectPage(ordersPage, lqw);

        List<Orders> ordersList = ordersPage.getRecords();
        List<OrderDto> orderDtoLIst = ordersList.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);

            LambdaQueryWrapper<Orders> orderLQW = new LambdaQueryWrapper<>();
            orderLQW.eq(Orders::getUserId, CommonThreadLocal.getUser());
            Integer count = orderMapper.selectCount(orderLQW);
            orderDto.setSumNum(count);

            LambdaQueryWrapper<OrderDetail> orderDetailLQW = new LambdaQueryWrapper<>();
            orderDetailLQW.eq(OrderDetail::getOrderId, orderDto.getId());
            List<OrderDetail> orderDetailList = orderDetailMapper.selectList(orderDetailLQW);
            orderDto.setOrderDetails(orderDetailList);

            return orderDto;
        }).collect(Collectors.toList());

        Page<OrderDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage, orderDtoPage, "records");

        orderDtoPage.setRecords(orderDtoLIst);

        return orderDtoPage;
    }

    @Override
    @Cacheable(key = "'page'")
    public Page<Orders> page(PageParam pageParam) {
        Page<Orders> ordersPage = new Page<Orders>(pageParam.getPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Orders::getOrderTime);
        orderMapper.selectPage(ordersPage, lqw);
        List<Orders> ordersList = ordersPage.getRecords();
        ordersList = ordersList.stream().map((item) -> {
            item.setUserName((item.getId().toString()));
            return item;
        }).collect(Collectors.toList());
        ordersPage.setRecords(ordersList);
        return ordersPage;
    }

    @Override
    @CacheEvict(beforeInvocation = true, key = "'page'")
    public Page<Orders> page(QueryPageParam pageParam) {
            Page<Orders> ordersPage = new Page<Orders>(pageParam.getPage(), pageParam.getPageSize());
            LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
            lqw.eq(pageParam.getNumber() != null, Orders::getId, pageParam.getNumber());
            lqw.between(pageParam.getBeginTime() != null,Orders::getOrderTime, pageParam.getBeginTime(), pageParam.getEndTime());
            lqw.orderByDesc(Orders::getOrderTime);
            orderMapper.selectPage(ordersPage, lqw);
            List<Orders> ordersList = ordersPage.getRecords();
            ordersList = ordersList.stream().map((item) -> {
                item.setUserName((item.getId().toString()));
                return item;
            }).collect(Collectors.toList());
            ordersPage.setRecords(ordersList);
            return ordersPage;
        }

    @Override
    @CacheEvict(key = "'page'")
    public String changeStatus(Orders orders) {
        Long id = orders.getId();
        Integer status = orders.getStatus();
        LambdaUpdateWrapper<Orders> luw = new LambdaUpdateWrapper<>();
        luw.eq(Orders::getId, id);
        luw.set(Orders::getStatus, status);
        orderMapper.update(orders, luw);
        return "修改成功";
    }
}
