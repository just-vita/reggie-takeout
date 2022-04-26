package com.learn.reggie.config;

import com.learn.reggie.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyUserService userService;
    @Autowired
    private DataSource datasource;
    @Autowired
    private PersistentTokenRepository tokenRepository;
    @Autowired
    private MyFailureHandler myFailureHandler;

    @Bean
    public PasswordEncoder pw(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .headers().frameOptions().sameOrigin();

        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .successForwardUrl("/success")
                .successHandler(new MyAuthenticationSuccessHandler("/backend/index.html"))
//                .failureForwardUrl("/error")
//                .failureHandler(new MyAuthenticationFailureHandler("/login.html"))
                .failureHandler(myFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password");

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/login.html").permitAll()
                .antMatchers("/backend/page/**").authenticated()
                .antMatchers("/backend/api/**").authenticated()
                .antMatchers("/backend/index.html").authenticated()
                .anyRequest().permitAll();

        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

        http.rememberMe()
                .userDetailsService(userService)
                .tokenRepository(tokenRepository);
//                .rememberMeServices();
        
        http.logout()
                .logoutSuccessUrl("/login.html")
                .logoutUrl("/logout");


    }

    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(datasource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
