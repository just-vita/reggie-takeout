package com.learn.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        if (CommonThreadLocal.getEmployee() != null){
            metaObject.setValue("createUser", CommonThreadLocal.getEmployee());
            metaObject.setValue("updateUser", CommonThreadLocal.getEmployee());
        }else{
            metaObject.setValue("createUser", CommonThreadLocal.getUser());
            metaObject.setValue("updateUser", CommonThreadLocal.getUser());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        if (CommonThreadLocal.getEmployee() != null){
            metaObject.setValue("updateUser", CommonThreadLocal.getEmployee());
        }else{
            metaObject.setValue("updateUser", CommonThreadLocal.getUser());
        }
    }
}
