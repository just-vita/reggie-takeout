package com.learn.reggie.service.impl;

import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.LoginUser;
import com.learn.reggie.service.LoginService;
import com.learn.reggie.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public R<Map> login(Employee employee) {
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(employee.getUsername(), password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        if (Objects.isNull(loginUser)){
            log.error("用户名或密码错误");
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        Long id = loginUser.getEmployee().getId();
        String token = JwtUtil.createToken(id.toString());
        HashMap<String, String> map = new HashMap<>();
        map.put("token",token);
        return R.success(map);
    }
}
