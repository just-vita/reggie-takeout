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
        Map<String, Object> map = JwtUtil.checkToken("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTEwNTg3NTEsInVzZXJJZCI6IjEyMyIsImlhdCI6MTY1MTA1NTE1MX0.-UfsDTWCn-_9UMkN7v4oEsyveDLC550sS9OiFPvSCsQ");
        Object userId = map.get("userId");
        System.out.println(userId);
    }
}
