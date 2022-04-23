package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.DishDto;
import com.learn.reggie.entity.Dish;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("page")
    public R<Page> page(PageParam pageParam){
        return dishService.page(pageParam);
    }

    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        return dishService.add(dishDto);
    }

    @GetMapping("{id}")
    public R<DishDto> getById(@PathVariable("id") Long id){
        return dishService.getById(id);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        return dishService.update(dishDto);
    }

    @DeleteMapping
    public R<String> delete(String ids){
        return dishService.delete(ids);
    }

    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,String ids){
        return dishService.changeStatus(status, ids);
    }

    @GetMapping("list")
    public R<List<DishDto>> list(Long categoryId){
        return dishService.list(categoryId);
    }
}
