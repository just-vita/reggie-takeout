package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.OrderDetail;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.mapper.EmployeeMapper;
import com.learn.reggie.mapper.OrderDetailMapper;
import com.learn.reggie.service.OrderDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
