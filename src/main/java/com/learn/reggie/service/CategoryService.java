package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.entity.Category;
import com.learn.reggie.entity.PageParam;

import java.util.List;

public interface CategoryService{
    Page<Category> getPage(PageParam pageParam);

    String add(Category category);

    String delete(Long id);

    String update(Category category);

    Category getById(Long id);

    List<Category> list(Category category);

}
