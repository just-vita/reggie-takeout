package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {

    List<AddressBook> AddressList();

    String add(AddressBook addressBook);

    String setDefault(AddressBook addressBook);

    AddressBook getDefault();

    String update(AddressBook addressBook);

    String delete(AddressBook addressBook);

    AddressBook getById(Long id);
}
