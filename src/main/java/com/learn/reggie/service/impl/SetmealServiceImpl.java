package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.reggie.common.R;
import com.learn.reggie.dto.SetmealDto;
import com.learn.reggie.entity.Category;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.entity.Setmeal;
import com.learn.reggie.entity.SetmealDish;
import com.learn.reggie.mapper.CategoryMapper;
import com.learn.reggie.mapper.SetmealDishMapper;
import com.learn.reggie.mapper.SetmealMapper;
import com.learn.reggie.service.SetmealDishService;
import com.learn.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "setmeal_page")
    public R<Page> page(PageParam pageParam) {
        Page<SetmealDto> setmealDtoPage = new Page<>();
        Page<Setmeal> page = new Page<>(
                pageParam.getPage(),
                pageParam.getPageSize());

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(
                StringUtils.isNotEmpty(pageParam.getName()),
                Setmeal::getName,
                pageParam.getName());
        lqw.orderByDesc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(page, lqw);

        List<Setmeal> records = page.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);

            LambdaQueryWrapper<SetmealDish> setmealDishLqw = new LambdaQueryWrapper<>();
            setmealDishLqw.eq(SetmealDish::getSetmealId,setmealDto.getId());
            List<SetmealDish> setmealDishList = setmealDishMapper.selectList(setmealDishLqw);
            setmealDto.setSetmealDishes(setmealDishList);

            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        BeanUtils.copyProperties(page,setmealDtoPage,"records");
        setmealDtoPage.setRecords(setmealDtoList);

        return R.success(setmealDtoPage);
    }

    @Override
    @Transactional
    public R<String> add(SetmealDto setmealDto) {
        setmealMapper.insert(setmealDto);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList.stream().map((item)->{
            Long setmealDtoId = setmealDto.getId();
            item.setSetmealId(setmealDtoId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);

        return R.success("添加成功");
    }

    @Override
    @Transactional
    public R<String> delete(String ids) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SetmealDish::getSetmealId, id);
            setmealMapper.deleteById(id);
            setmealDishMapper.delete(lqw);
        }
        return R.success("删除成功");
    }

    @Override
    @Transactional
    public R<String> update(SetmealDto setmealDto) {
        setmealMapper.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishMapper.delete(lqw);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList = setmealDishList.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
        return R.success("修改成功");
    }

    @Override
    public R<SetmealDto> getById(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getId, setmeal.getCategoryId());
        lqw.select(Category::getName);
        Category category = categoryMapper.selectOne(lqw);
        setmealDto.setCategoryName(category.getName());

        LambdaQueryWrapper<SetmealDish> setmealDishLqw = new LambdaQueryWrapper<>();
        setmealDishLqw.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(setmealDishLqw);
        setmealDto.setSetmealDishes(setmealDishList);

        return R.success(setmealDto);
    }

    @Override
    public R<String> changeStatus(String ids, Integer status) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Setmeal setmeal = setmealMapper.selectById(id);
            setmeal.setStatus(status);
            setmealMapper.updateById(setmeal);
        }

        return R.success("修改成功");
    }

    @Override
    public R<List<Setmeal>> list(Setmeal setmeal) {
        Long categoryId = setmeal.getCategoryId();
        Integer status = setmeal.getStatus();

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getCategoryId, categoryId);
        lqw.eq(Setmeal::getStatus, status);
        lqw.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealMapper.selectList(lqw);

        return R.success(setmealList);
    }


}
