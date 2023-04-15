package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.dto.SetmealDto;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.QueryPageParam;
import com.learn.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService {

    Page<SetmealDto> page(PageParam pageParam);

    Page<SetmealDto> page(QueryPageParam pageParam);

    String add(SetmealDto setmealDto);

    String delete(String ids);

    String update(SetmealDto setmealDto);

    SetmealDto getById(Long id);

    String changeStatus(String ids, Integer status);

    List<Setmeal> list(Setmeal setmeal);
}
