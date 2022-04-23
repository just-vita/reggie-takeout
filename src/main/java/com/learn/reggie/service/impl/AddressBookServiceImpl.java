package com.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.reggie.common.CommonThreadLocal;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.AddressBook;
import com.learn.reggie.mapper.AddressBookMapper;
import com.learn.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public R<List<AddressBook>> AddressList() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, CommonThreadLocal.getUser());
        lqw.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBookList = addressBookMapper.selectList(lqw);
        return R.success(addressBookList);
    }

    @Override
    public R<String> add(AddressBook addressBook) {
        addressBook.setUserId(CommonThreadLocal.getUser());
        addressBookMapper.insert(addressBook);
        return R.success("添加成功");
    }

    @Override
    @Transactional
    public R<String> setDefault(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId, CommonThreadLocal.getUser());
        luw.set(AddressBook::getIsDefault, 0);
        this.update(luw);

        addressBook.setIsDefault(1);
        addressBookMapper.updateById(addressBook);
        return R.success("修改成功");
    }

    @Override
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, CommonThreadLocal.getUser());
        lqw.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookMapper.selectOne(lqw);
        return R.success(addressBook);
    }

    @Override
    public R<String> update(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
        return R.success("修改成功");
    }

    @Override
    public R<String> delete(AddressBook addressBook) {
        addressBookMapper.deleteById(addressBook);
        return R.success("删除成功");
    }

    @Override
    public R<AddressBook> getById(Long id) {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getId, id);
        AddressBook addressBook = addressBookMapper.selectOne(lqw);
        return R.success(addressBook);
    }
}
