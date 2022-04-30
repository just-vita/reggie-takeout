package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.UserDto;
import com.learn.reggie.entity.User;
import com.learn.reggie.mapper.UserMapper;
import com.learn.reggie.service.UserService;
import com.learn.reggie.utils.CodeUtils;
import com.learn.reggie.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CodeUtils codeUtils;
    @Autowired
    private RedisUtil redisUtil;

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
            redisUtil.set("loginUser", user.getId());
            return R.success(user);
        }
        return R.error("验证码有误");
    }

    @Override
    public R<String> loginout() {
        redisUtil.remove("loginUser");
        return R.success("退出成功");
    }
}
