package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.common.CustomException;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.Category;
import com.learn.reggie.entity.Dish;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.Setmeal;
import com.learn.reggie.mapper.CategoryMapper;
import com.learn.reggie.mapper.DishMapper;
import com.learn.reggie.mapper.SetmealMapper;
import com.learn.reggie.service.CategoryService;
import com.learn.reggie.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    @Cacheable(value = "category_page")
    public R<Page> getPage(PageParam pageParam) {
        Page<Category> page = new Page<>(pageParam.getPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.like(
                StringUtils.isNotEmpty(pageParam.getName()),
                Category::getName,
                pageParam.getName());
        lqw.orderByDesc(Category::getUpdateTime);
        lqw.orderByAsc(Category::getSort);
        categoryMapper.selectPage(page, lqw);
        return R.success(page);
    }

    @Override
    public R<String> add(Category category) {
        categoryMapper.insert(category);
        redisUtil.remove("category_page");
        return R.success("添加成功");
    }

    @Override
    public R<String> delete(Long id) {
        LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<Dish>();
        dishlqw.eq(Dish::getCategoryId, id);
        Integer count1 = dishMapper.selectCount(dishlqw);
        if (count1 > 0) {
            throw new CustomException("此分类下关联了菜品，无法删除");
        }

        LambdaQueryWrapper<Setmeal> setmeallqw = new LambdaQueryWrapper<>();
        setmeallqw.eq(Setmeal::getCategoryId, id);
        Integer count2 = setmealMapper.selectCount(setmeallqw);
        if (count2 > 0) {
            throw new CustomException("此分类下关联了套餐，无法删除");
        }

        categoryMapper.deleteById(id);
        redisUtil.remove("category_page");

        return R.success("删除成功");
    }

    @Override
    public R<String> update(Category category) {
        Long id = category.getId();
        Category fromDb = categoryMapper.selectById(id);
        fromDb.setName(category.getName());
        fromDb.setSort(category.getSort());
        categoryMapper.updateById(fromDb);
        redisUtil.remove("category_page");
        return R.success("编辑成功");
    }

    @Override
    public R<Category> getById(Long id) {
        Category category = categoryMapper.selectById(id);
        return R.success(category);
    }

    @Override
    @Cacheable(value = "category_list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(
                category.getType() != null,
                Category::getType,
                category.getType());
        lqw.orderByAsc(Category::getUpdateTime);
        List<Category> categories = categoryMapper.selectList(lqw);
        return R.success(categories);
    }
}
