package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.mapper.EmployeeMapper;
import com.learn.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());

        Employee login = employeeMapper.selectOne(lqw);

        if (login == null) {
            return R.error("用户名不存在");
        }

        if (!login.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        if (login.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee", login.getId());

        return R.success(login);
    }

    @Override
    public R<String> logout() {
        request.getSession().removeAttribute("employee");
        return R.success("登出成功");
    }

    @Override
    public R<String> add(Employee employee) {

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeMapper.insert(employee);

        return R.success("添加成功");
    }

    @Override
    public R<Page> findAll(PageParam pageParam) {
        Page<Employee> page = new Page<>(pageParam.getPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(
                StringUtils.isNotEmpty(pageParam.getName()),
                Employee::getUsername,
                pageParam.getName());
        lqw.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(page, lqw);
        return R.success(page);
    }

    @Override
    public R<String> changeStatus(Employee employee) {
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
        return R.success("success");
    }

    @Override
    public R<Employee> getEmployeeById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        return R.success(employee);
    }

}
