package com.learn.reggie.controller;

import com.learn.reggie.common.R;
import com.learn.reggie.dto.UserDto;
import com.learn.reggie.entity.User;
import com.learn.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("sendMsg")
    public R<String> sendMsg(@RequestBody User user){
        return userService.sendMsg(user);
    }

    @PostMapping("login")
    public R<User> login(@RequestBody UserDto userDto){
        return userService.login(userDto);
    }

    @PostMapping("loginout")
    public R<String> loginout(){
        return userService.loginout();
    }
}
