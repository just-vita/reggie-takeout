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
    public R<Page<Category>> getPage(PageParam pageParam){
        Page<Category> page = categoryService.getPage(pageParam);
        return R.success(page);
    }

    @PostMapping
    public R<String> add(@RequestBody Category category){
        String info = categoryService.add(category);
        return R.success(info);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        String info = categoryService.delete(ids);
        return R.success(info);
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        String info = categoryService.update(category);
        return R.success(info);
    }

    @GetMapping("{id}")
    public R<Category> getById(@PathVariable("id") Long id){
        Category category = categoryService.getById(id);
        return R.success(category);
    }

    @GetMapping("list")
    public R<List<Category>> list(Category category){
        List<Category> categoryList = categoryService.list(category);
        return R.success(categoryList);
    }

}
