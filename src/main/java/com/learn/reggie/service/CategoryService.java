package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Category;
import com.learn.reggie.entity.PageParam;

import java.util.List;

public interface CategoryService{
    R<Page> getPage(PageParam pageParam);

    R<String> add(Category category);

    R<String> delete(Long id);

    R<String> update(Category category);

    R<Category> getById(Long id);

    R<List<Category>> list(Category category);

}
