package com.learn.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, s);
        Employee employee = employeeMapper.selectOne(lqw);

//        if (!"admin".equals(s)){
//            throw new UsernameNotFoundException("username no find");
//        }
        if (employee == null){
            throw new UsernameNotFoundException("username no find");
        }

        String password = passwordEncoder.encode("123456");

        return new User(employee.getUsername(),password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user,ROLE_admin"));
    }
}
