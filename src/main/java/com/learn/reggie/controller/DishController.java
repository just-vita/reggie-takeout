package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.DishDto;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.QueryPageParam;
import com.learn.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("page")
    public R<Page<DishDto>> page(QueryPageParam queryPageParam) {
        if (queryPageParam.getName() == null) {
            PageParam pageParam = new PageParam();
            BeanUtils.copyProperties(queryPageParam, pageParam);
            return page(pageParam);
        }
        Page<DishDto> page = dishService.page(queryPageParam);
        return R.success(page);
    }

    /***
     * 因为缓存的原因，带查询条件时需要用一个新的方法来接收，实现带查询条件时缓存的清除
     */
    public R<Page<DishDto>> page(PageParam pageParam) {
        Page<DishDto> page = dishService.page(pageParam);
        return R.success(page);
    }

    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto) {
        String info = dishService.add(dishDto);
        return R.success(info);
    }

    @GetMapping("{id}")
    public R<DishDto> getById(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.getById(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        String info = dishService.update(dishDto);
        return R.success(info);
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        String info = dishService.delete(ids);
        return R.success(info);
    }

    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable Integer status, String ids) {
        String info = dishService.changeStatus(status, ids);
        return R.success(info);
    }

    @GetMapping("list")
    public R<List<DishDto>> list(Long categoryId) {
        List<DishDto> dtoList = dishService.list(categoryId);
        return R.success(dtoList);
    }
}
