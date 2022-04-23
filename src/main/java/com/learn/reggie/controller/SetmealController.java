package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.SetmealDto;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.Setmeal;
import com.learn.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @GetMapping("page")
    public R<Page> page(PageParam pageParam) {
        return setmealService.page(pageParam);
    }

    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto) {
        return setmealService.add(setmealDto);
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        return setmealService.delete(ids);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        return setmealService.update(setmealDto);
    }

    @GetMapping("{id}")
    public R<SetmealDto> getById(@PathVariable("id") Long id) {
        return setmealService.getById(id);
    }

    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable Integer status, String ids) {
        return setmealService.changeStatus(ids, status);
    }

    @GetMapping("list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        return setmealService.list(setmeal);
    }
}
