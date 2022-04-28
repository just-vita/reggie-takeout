package com.learn.reggie.controller;

import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SecurityController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public R<Map> login(@RequestBody Employee employee){
        System.out.println(employee);
        return loginService.login(employee);
    }
}
