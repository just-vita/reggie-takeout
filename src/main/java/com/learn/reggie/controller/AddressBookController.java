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
        return addressBookService.AddressList();
    }

    @PostMapping
    public R<String> add(@RequestBody AddressBook addressBook){
        return addressBookService.add(addressBook);
    }

    @PutMapping("default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        return addressBookService.setDefault(addressBook);
    }

    @GetMapping("default")
    public R<AddressBook> getDefault(){
        return addressBookService.getDefault();
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        return addressBookService.update(addressBook);
    }

    @DeleteMapping
    public R<String> delete(@RequestBody AddressBook addressBook){
        return addressBookService.delete(addressBook);
    }

    @GetMapping("{id}")
    public R<AddressBook> getById(@PathVariable("id") Long id){
        return addressBookService.getById(id);
    }
}
