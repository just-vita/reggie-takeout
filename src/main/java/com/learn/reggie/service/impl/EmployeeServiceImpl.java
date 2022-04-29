package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.mapper.EmployeeMapper;
import com.learn.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
@CacheConfig(cacheNames = {"employee_page"})
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public String logout() {
        System.out.println("into method...");
        System.out.println(request.getSession().getAttribute("employee"));
        request.getSession().removeAttribute("employee");
        return "登出成功";
    }

    @Override
    @CacheEvict(key = "'page'")
    public String add(Employee employee) {

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        String password = passwordEncoder.encode("123456");
//        employee.setPassword(password);

        employeeMapper.insert(employee);

        return "添加成功";
    }

    @Override
    @Cacheable(key = "'page'")
    public Page<Employee> findAll(PageParam pageParam) {
        Page<Employee> page = new Page<>(pageParam.getPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(
                StringUtils.isNotEmpty(pageParam.getName()),
                Employee::getUsername,
                pageParam.getName());
        lqw.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(page, lqw);
        return page;
    }

    @Override
    @CacheEvict(key = "'page'")
    public String changeStatus(Employee employee) {
        Long id = employee.getId();
        Employee fromDB = employeeMapper.selectById(id);
        if (employee.getUsername() != null) {
            fromDB.setUsername(employee.getUsername());
            fromDB.setName(employee.getName());
            fromDB.setPhone(employee.getPhone());
            fromDB.setSex(employee.getSex());
            fromDB.setIdNumber(employee.getIdNumber());
        }
        fromDB.setStatus(fromDB.getStatus() == 1 ? 0 : 1);
        employeeMapper.updateById(fromDB);
        return "success";
    }

    @Override
    @Cacheable(value = "employeeById",key = "#id")
    public Employee getEmployeeById(Long id) {
        if (id == null){
            return null;
        }
        return employeeMapper.selectById(id);
    }

}
