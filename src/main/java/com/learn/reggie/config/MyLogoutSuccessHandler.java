package com.learn.reggie.config;

import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.entity.LoginUser;
import com.learn.reggie.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        log.info("用户 "+loginUser.getEmployee().getUsername()+" 退出登录成功");
        String redisStr = "login:" + loginUser.getEmployee().getId();
        redisUtil.remove(redisStr);
        CommonThreadLocal.removeEmployee();
    }
}
