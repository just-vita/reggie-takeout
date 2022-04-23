package com.learn.reggie.service;

import com.learn.reggie.common.R;
import com.learn.reggie.dto.UserDto;
import com.learn.reggie.entity.User;

public interface UserService {

    R<String> sendMsg(User user);

    R<User> login(UserDto userDto);

    R<String> loginout();
}
