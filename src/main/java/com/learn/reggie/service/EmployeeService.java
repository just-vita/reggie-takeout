package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.QueryPageParam;

public interface EmployeeService extends IService<Employee> {

    String logout();

    String add(Employee employee);

    Page<Employee> findAll(PageParam pageParam);

    Page<Employee> findAll(QueryPageParam pageParam);

    String changeStatus(Employee employee);

    Employee getEmployeeById(Long id);
}
