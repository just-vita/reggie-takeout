package com.learn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Category;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("page")
    public R<Page> getPage(PageParam pageParam){
        return categoryService.getPage(pageParam);
    }

    @PostMapping
    public R<String> add(@RequestBody Category category){
        return categoryService.add(category);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        return categoryService.delete(ids);
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        return categoryService.update(category);
    }

    @GetMapping("{id}")
    public R<Category> getById(@PathVariable("id") Long id){
        return categoryService.getById(id);
    }

    @GetMapping("list")
    public R<List<Category>> list(Category category){
        return categoryService.list(category);
    }

}
