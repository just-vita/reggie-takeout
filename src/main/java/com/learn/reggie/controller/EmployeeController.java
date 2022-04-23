package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        return employeeService.login(request,employee);
    }

    @PostMapping("logout")
    public R<String> logout(){
        return employeeService.logout();
    }

    @PostMapping
    public R<String> add(@RequestBody Employee employee){
        return employeeService.add(employee);
    }

    @GetMapping("page")
    public R<Page> page(PageParam pageParam){
        return employeeService.findAll(pageParam);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        return employeeService.changeStatus(employee);
    }

    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        return employeeService.getEmployeeById(id);
    }

}
