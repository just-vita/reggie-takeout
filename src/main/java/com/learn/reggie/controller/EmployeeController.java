package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public R<String> add(@RequestBody Employee employee){
        String info = employeeService.add(employee);
        return R.success(info);
    }

    @GetMapping("page")
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
