package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.SetmealDto;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService {

    R<Page> page(PageParam pageParam);

    R<String> add(SetmealDto setmealDto);

    R<String> delete(String ids);

    R<String> update(SetmealDto setmealDto);

    R<SetmealDto> getById(Long id);

    R<String> changeStatus(String ids, Integer status);

    R<List<Setmeal>> list(Setmeal setmeal);
}
