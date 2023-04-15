package com.learn.reggie.controller;

import com.learn.reggie.common.R;
import com.learn.reggie.entity.AddressBook;
import com.learn.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("list")
    public R<List<AddressBook>> list(){
        List<AddressBook> addressBookList = addressBookService.AddressList();
        return R.success(addressBookList);
    }

    @PostMapping
    public R<String> add(@RequestBody AddressBook addressBook){
        String info = addressBookService.add(addressBook);
        return R.success(info);
    }

    @PutMapping("default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        String info = addressBookService.setDefault(addressBook);
        return R.success(info);
    }

    @GetMapping("default")
    public R<AddressBook> getDefault(){
        AddressBook addressBook = addressBookService.getDefault();
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        String info = addressBookService.update(addressBook);
        return R.success(info);
    }

    @DeleteMapping
    public R<String> delete(@RequestBody AddressBook addressBook){
        String info = addressBookService.delete(addressBook);
        return R.success(info);
    }

    @GetMapping("{id}")
    public R<AddressBook> getById(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }
}
