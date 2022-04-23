package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.UserDto;
import com.learn.reggie.entity.User;
import com.learn.reggie.mapper.UserMapper;
import com.learn.reggie.service.UserService;
import com.learn.reggie.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CodeUtils codeUtils;

    @Override
    public R<String> sendMsg(User user) {
        String code = codeUtils.generator(user.getPhone());
        log.info("code ----> "+code);
        return R.success("短信验证码发送成功");
    }

    @Override
    public R<User> login(UserDto userDto) {
        String code = userDto.getCode();
        String phone = userDto.getPhone();
        Object codeFromCache = codeUtils.get(phone);
        if (code.equals(codeFromCache)){
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userMapper.selectOne(lqw);
            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userMapper.insert(user);
            }
            CommonThreadLocal.setUserLocal(user.getId());
            request.getSession().setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("验证码有误");
    }

    @Override
    public R<String> loginout() {
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
