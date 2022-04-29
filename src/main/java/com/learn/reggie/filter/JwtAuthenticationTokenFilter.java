package com.learn.reggie.filter;

import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.entity.LoginUser;
import com.learn.reggie.utils.JwtUtil;
import com.learn.reggie.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtil redisUtil;
    private final List<String> urlList = new ArrayList<>(Arrays.asList(
            "html","css","js","png","woff","ico","map","woff2"
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        String requestURI = request.getRequestURI();
        for (String url : urlList) {
            if (requestURI.endsWith(url)){
                filterChain.doFilter(request,response);
                return;
            }
        }
        if (requestURI.equals("/login") || requestURI.equals("/common/download")){
            filterChain.doFilter(request,response);
            return;
        }

        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        String str = "{\"code\":0,\"msg\":\"NOTLOGIN\"}";
        byte[] b=str.getBytes();

        if (Objects.isNull(token) || "null".equals(token)){
            log.error("用户未登录");
            outputStream.write(b);
            outputStream.close();
            return;
        }

        Map<String, Object> tokenMap = JwtUtil.checkToken(token);
        if (tokenMap == null){
            outputStream.write(b);
            outputStream.close();
            return;
        }

        String userId = (String) tokenMap.get("userId");
        String redisKey = "login:" + userId;
        LoginUser loginUser = (LoginUser) redisUtil.get(redisKey);
        if (Objects.isNull(loginUser)){
            log.error("用户未登录");
            outputStream.write(b);
            outputStream.close();
            return;
        }
//        log.info("从redis中获取到了：" + loginUser.getUsername());

        CommonThreadLocal.setEmployeeLocal(loginUser.getEmployee().getId());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginUser, null, null);
        SecurityContextHolder.getContext().
                setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }

}
