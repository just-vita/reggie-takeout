package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.DishDto;
import com.learn.reggie.entity.PageParam;

import java.util.List;

public interface DishService {
    R<Page> page(PageParam pageParam);

    R<String> add(DishDto dishDto);

    R<DishDto> getById(Long id);

    R<String> update(DishDto dishDto);

    R<String> delete(String ids);

    R<String> changeStatus(Integer status, String ids);

    R<List<DishDto>> list(Long categoryId);
}
