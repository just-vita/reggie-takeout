package com.learn.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.LoginUser;
import com.learn.reggie.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, username);
        Employee employee = employeeMapper.selectOne(lqw);

        if (employee == null){
            throw new UsernameNotFoundException("username no find");
        }

//        String password = passwordEncoder.encode("123456");
        String password = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(password);
//        String password = DigestUtils.md5DigestAsHex("123456".getBytes());

        return new LoginUser(employee);
    }
}
