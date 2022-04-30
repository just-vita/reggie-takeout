package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.entity.AddressBook;
import com.learn.reggie.mapper.AddressBookMapper;
import com.learn.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"address_page"})
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    @Cacheable(key = "'page'")
    public List<AddressBook> AddressList() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, CommonThreadLocal.getUser());
        lqw.orderByDesc(AddressBook::getUpdateTime);
        return addressBookMapper.selectList(lqw);
    }

    @Override
    @CacheEvict(key = "'page'")
    public String add(AddressBook addressBook) {
        addressBook.setUserId(CommonThreadLocal.getUser());
        addressBookMapper.insert(addressBook);
        return "添加成功";
    }

    @Override
    @Transactional
    @CacheEvict(key = "'page'")
    public String setDefault(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId, CommonThreadLocal.getUser());
        luw.set(AddressBook::getIsDefault, 0);
        this.update(luw);

        addressBook.setIsDefault(1);
        addressBookMapper.updateById(addressBook);
        return "修改成功";
    }

    @Override
    public AddressBook getDefault() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, CommonThreadLocal.getUser());
        lqw.eq(AddressBook::getIsDefault, 1);
        return addressBookMapper.selectOne(lqw);
    }

    @Override
    @CacheEvict(key = "'page'")
    public String update(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
        return "修改成功";
    }

    @Override
    @CacheEvict(key = "'page'")
    public String delete(AddressBook addressBook) {
        addressBookMapper.deleteById(addressBook);
        return "删除成功";
    }

    @Override
    @Cacheable(value = "address_getById",key = "#id")
    public AddressBook getById(Long id) {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getId, id);
        return addressBookMapper.selectOne(lqw);
    }
}
