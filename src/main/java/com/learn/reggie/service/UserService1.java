package com.learn.reggie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService1 implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("method is running...");
        if (!"admin".equals(s)){
            throw new UsernameNotFoundException("username no find");
        }

        String password = passwordEncoder.encode("123456");

        return new User("admin",password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user,ROLE_admin"));
    }
}
