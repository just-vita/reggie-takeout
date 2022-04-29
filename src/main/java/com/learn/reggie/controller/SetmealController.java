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
        Page<SetmealDto> page = setmealService.page(pageParam);
        return R.success(page);
    }

    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto) {
        String info = setmealService.add(setmealDto);
        return R.success(info);
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        String info = setmealService.delete(ids);
        return R.success(info);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        String info = setmealService.update(setmealDto);
        return R.success(info);
    }

    @GetMapping("{id}")
    public R<SetmealDto> getById(@PathVariable("id") Long id) {
        SetmealDto setmealDto = setmealService.getById(id);
        return R.success(setmealDto);
    }

    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable Integer status, String ids) {
        String info = setmealService.changeStatus(ids, status);
        return R.success(info);
    }

    @GetMapping("list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        List<Setmeal> list = setmealService.list(setmeal);
        return R.success(list);
    }
}
