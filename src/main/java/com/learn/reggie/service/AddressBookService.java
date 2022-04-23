package com.learn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.reggie.common.R;
import com.learn.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {

    R<List<AddressBook>> AddressList();

    R<String> add(AddressBook addressBook);

    R<String> setDefault(AddressBook addressBook);

    R<AddressBook> getDefault();

    R<String> update(AddressBook addressBook);

    R<String> delete(AddressBook addressBook);

    R<AddressBook> getById(Long id);
}
