package com.learn.reggie;

import com.learn.reggie.entity.Employee;
import com.learn.reggie.service.EmployeeService;
import com.learn.reggie.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void test()  {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setStatus(1);
        employeeService.changeStatus(employee);
    }

    @Test
    public void TokenTest(){
        String token = JwtUtil.createToken("123");
        System.out.println(token);
    }

    @Test
    public void TokenCheck(){
        Map<String, Object> map = JwtUtil.checkToken("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTExNDI4NTQsInVzZXJJZCI6IjEyMyIsImlhdCI6MTY1MTEzOTI1NH0.7XVBBqOjgj8x4xLPUHE-57Or1R96xYawhmj25yDNIR0");
//        Object userId = map.get("userId");
//        System.out.println(userId);
        for (String s : map.keySet()) {
            System.out.println(s+" : "+map.get(s));
        }
    }
}
