package com.learn.reggie;

import com.learn.reggie.entity.Employee;
import com.learn.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
