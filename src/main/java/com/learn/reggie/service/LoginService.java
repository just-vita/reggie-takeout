package com.learn.reggie.service;

import com.learn.reggie.common.R;
import com.learn.reggie.entity.Employee;

import java.util.Map;

public interface LoginService {
    R<Map> login(Employee employee);
}
