package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.reggie.dto.DishDto;
import com.learn.reggie.entity.Category;
import com.learn.reggie.entity.Dish;
import com.learn.reggie.entity.DishFlavor;
import com.learn.reggie.entity.PageParam;
import com.learn.reggie.mapper.CategoryMapper;
import com.learn.reggie.mapper.DishFlavorMapper;
import com.learn.reggie.mapper.DishMapper;
import com.learn.reggie.service.DishFlavorService;
import com.learn.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@CacheConfig(cacheNames = {"dish_page"})
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(key = "'page'")
    public Page<DishDto> page(PageParam pageParam) {
        Page<DishDto> dishDtoPage = new Page<>();
        Page<Dish> page = new Page<>(
                pageParam.getPage(),
                pageParam.getPageSize());

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(
                StringUtils.isNotEmpty(pageParam.getName()),
                Dish::getName,
                pageParam.getName());
        lqw.orderByDesc(Dish::getUpdateTime);
        dishMapper.selectPage(page, lqw);

        // 因为dish实体类中没有分类名称字段，所以使用DTO对象返回，将分类名称设置进DTO的page对象中
        List<Dish> dishList = page.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            String categoryName = category.getName();

            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        BeanUtils.copyProperties(page,dishDtoPage,"records");
        dishDtoPage.setRecords(dishDtoList);

        return dishDtoPage;
    }

    @Override
    @CacheEvict(key = "'page'")
    public String add(DishDto dishDto) {
        dishMapper.insert(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
        dishFlavorService.saveBatch(flavorList);
        return "添加成功";
    }

    @Override
    @Cacheable(value = "dish_getById",key = "#id")
    public DishDto getById(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishMapper.selectById(id);

        BeanUtils.copyProperties(dish, dishDto);

        Long categoryId = dish.getCategoryId();
        Category category = categoryMapper.selectById(categoryId);
        String categoryName = category.getName();

        dishDto.setCategoryName(categoryName);

        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(lqw);

        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    @Override
    @CacheEvict(key = "'page'")
    public String update(DishDto dishDto) {
        dishMapper.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorMapper.delete(lqw);

        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList = flavorList.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavorList);

        return "修改成功";
    }

    @Override
    @CacheEvict(key = "'page'")
    public String delete(String ids) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
            lqw.eq(DishFlavor::getDishId, id);
            
            dishFlavorMapper.delete(lqw);
            dishMapper.deleteById(id);
        }

        return "删除成功";
    }

    @Override
    @CacheEvict(key = "'page'")
    public String changeStatus(Integer status, String ids) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Dish dish = dishMapper.selectById(id);
            dish.setStatus(status);
            dishMapper.updateById(dish);
        }
        return "修改成功";
    }

    @Override
    @Cacheable(value = "dish_list",key = "#categoryId")
    public List<DishDto> list(Long categoryId) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId, categoryId);
        lqw.orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishMapper.selectList(lqw);

        List<DishDto> dishDtoList = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            LambdaQueryWrapper<Category> clqw = new LambdaQueryWrapper<>();
            clqw.eq(Category::getId,dishDto.getCategoryId());
            clqw.select(Category::getName);
            String categoryName = categoryMapper.selectOne(clqw).getName();

            LambdaQueryWrapper<DishFlavor> dflqw = new LambdaQueryWrapper<>();
            dflqw.eq(DishFlavor::getDishId, dishDto.getId());
            List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(dflqw);

            dishDto.setCategoryName(categoryName);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());


        return dishDtoList;
    }


}
