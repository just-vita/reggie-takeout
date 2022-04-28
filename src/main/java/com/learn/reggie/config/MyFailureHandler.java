package com.learn.reggie.config;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class MyFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public MyFailureHandler() {
        this.setDefaultFailureUrl("/login.html?error");
    }
}
