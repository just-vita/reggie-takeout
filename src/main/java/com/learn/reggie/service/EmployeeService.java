package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.PageParam;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    R<Employee> login(HttpServletRequest request, Employee employee);

    R<String> logout();

    R<String> add(Employee employee);

    R<Page> findAll(PageParam pageParam);

    R<String> changeStatus(Employee employee);

    R<Employee> getEmployeeById(Long id);
}
