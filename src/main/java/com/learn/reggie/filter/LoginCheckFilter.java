package com.learn.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();// /backend/index.html

//        log.info("拦截到请求：{}", requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/login.html",
                "/login",
                "/login/**",
                "/favicon.ico",
                "/employee/**",
                "/category/**",
                "/setmeal/**",
                "/dish/**",
                "/order/**",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/common/download",
                "/common/upload",
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if (check) {
//            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

//        //4.1、判断登录状态，如果已登录，则直接放行
//        Object employeeId = request.getSession().getAttribute("employee");
//        if (employeeId != null) {
//
//            log.info("用户已登录后台，用户id为：{}", employeeId);
//
//            CommonThreadLocal.setEmployeeLocal((Long) employeeId);
//
//            filterChain.doFilter(request, response);
//            return;
//        }

        //4.2、判断登录状态，如果已登录，则直接放行
        Object userId = request.getSession().getAttribute("user");
        if (userId != null) {
            log.info("用户已登录前台，用户id为：{}", userId);

            CommonThreadLocal.setUserLocal((Long) userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(R.error("NOTLOGIN")));
        out.flush();
        out.close();
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
