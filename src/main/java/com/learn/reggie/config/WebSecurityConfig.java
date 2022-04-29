package com.learn.reggie.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.LoginUser;
import com.learn.reggie.filter.JwtAuthenticationTokenFilter;
import com.learn.reggie.filter.LoginFilter;
import com.learn.reggie.service.MyUserService;
import com.learn.reggie.utils.JwtUtil;
import com.learn.reggie.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyUserService userService;
    @Autowired
    private DataSource datasource;
//    @Autowired
//    private PersistentTokenRepository tokenRepository;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private RedisUtil redisUtil;

    @Bean
    public PasswordEncoder pw(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .headers().frameOptions().sameOrigin();

        // 相关属性在loginFilter设置了
        http.formLogin()
                .loginPage("/login.html");
//                .successHandler(new MyAuthenticationSuccessHandler("/backend/index.html"))
//                .failureHandler(myFailureHandler);

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/login.html").permitAll()
                .antMatchers("/backend/api/login.js").permitAll()
                .antMatchers("/backend/plugins/**").permitAll()
                .antMatchers("/backend/styles/**").permitAll()
                .antMatchers("/backend/images/**").permitAll()
                .antMatchers("/backend/js/**").permitAll()
                .antMatchers("/common/**").permitAll()
                .antMatchers("/login").permitAll()
//                .antMatchers("/backend/page/**").authenticated()
//                .antMatchers("/backend/api/**").authenticated()
//                .antMatchers("/backend/index.html").authenticated()
                .anyRequest().permitAll();

        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

//        http.rememberMe()
//                .userDetailsService(userService)
//                .tokenRepository(tokenRepository);
//                .rememberMeServices();
        
        http.logout()
                .logoutSuccessUrl("/login.html")
                .logoutUrl("/logout");
    }

//    @Bean
//    public PersistentTokenRepository tokenRepository(){
//        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        jdbcTokenRepository.setDataSource(datasource);
////        jdbcTokenRepository.setCreateTableOnStartup(true);
//        return jdbcTokenRepository;
//    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();

                redisUtil.set("login:"+loginUser.getEmployee().getId(),loginUser);
                CommonThreadLocal.setEmployeeLocal(loginUser.getEmployee().getId());

                Long id = loginUser.getEmployee().getId();
                String token = JwtUtil.createToken(id.toString());
                R<String> ok = R.success(token);
                String s = new ObjectMapper().writeValueAsString(ok);
                out.write(s);
                out.flush();
                out.close();
            }
        });
        loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException, JsonProcessingException {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                R respBean = R.error(exception.getMessage());
                if (exception instanceof LockedException) {
                    respBean.setMsg("账户被锁定，请联系管理员!");
                } else if (exception instanceof CredentialsExpiredException) {
                    respBean.setMsg("密码过期，请联系管理员!");
                } else if (exception instanceof AccountExpiredException) {
                    respBean.setMsg("账户过期，请联系管理员!");
                } else if (exception instanceof DisabledException) {
                    respBean.setMsg("账户被禁用，请联系管理员!");
                } else if (exception instanceof BadCredentialsException) {
                    respBean.setMsg("用户名或者密码输入错误，请重新输入!");
                }
                out.write(new ObjectMapper().writeValueAsString(respBean));
                out.flush();
                out.close();
            }
        });
        loginFilter.setAuthenticationManager(authenticationManager());
        loginFilter.setFilterProcessesUrl("/login");
        return loginFilter;
    }
}
