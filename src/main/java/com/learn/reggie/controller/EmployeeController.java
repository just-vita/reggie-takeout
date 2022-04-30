package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.QueryPageParam;
import com.learn.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("logout")
    public R<String> logout(){
        String info = employeeService.logout();
        return R.success(info);
    }

    @PostMapping
    @PreAuthorize("principal.username.equals('admin')")
    public R<String> add(@RequestBody Employee employee){
        String info = employeeService.add(employee);
        return R.success(info);
    }

    @GetMapping("page")
    @PreAuthorize("principal.username.equals('admin')")
    public R<Page<Employee>> page(QueryPageParam queryPageParam){
        if (queryPageParam.getName() == null){
            PageParam pageParam = new PageParam();
            BeanUtils.copyProperties(queryPageParam,pageParam);
            return page(pageParam);
        }
        Page<Employee> page = employeeService.findAll(queryPageParam);
        return R.success(page);
    }

    /***
     * 因为缓存的原因，带查询条件时需要用一个新的方法来接收，实现带查询条件时缓存的清除
     */
    public R<Page<Employee>> page(PageParam pageParam){
        Page<Employee> page = employeeService.findAll(pageParam);
        return R.success(page);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        String info = employeeService.changeStatus(employee);
        return R.success(info);
    }

    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        Employee employeeById = employeeService.getEmployeeById(id);
        return R.success(employeeById);
    }

}
