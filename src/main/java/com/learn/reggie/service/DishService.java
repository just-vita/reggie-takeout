package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.dto.DishDto;
import com.learn.reggie.entity.PageParam;

import java.util.List;

public interface DishService {
    Page<DishDto> page(PageParam pageParam);

    String add(DishDto dishDto);

    DishDto getById(Long id);

    String update(DishDto dishDto);

    String delete(String ids);

    String changeStatus(Integer status, String ids);

    List<DishDto> list(Long categoryId);
}
