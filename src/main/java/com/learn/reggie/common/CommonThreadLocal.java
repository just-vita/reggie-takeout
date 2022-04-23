package com.learn.reggie.common;


public class CommonThreadLocal {
    private CommonThreadLocal(){};

    private static final ThreadLocal<Long> EMPLOYEE_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<Long> USER_LOCAL = new ThreadLocal<>();

    public static void setEmployeeLocal(Long id){
        EMPLOYEE_LOCAL.set(id);
    }

    public static Long getEmployee(){
        return EMPLOYEE_LOCAL.get();
    }

    public static void removeEmployee(){
        EMPLOYEE_LOCAL.remove();
    }

    public static void setUserLocal(Long id){
        USER_LOCAL.set(id);
    }

    public static Long getUser(){
        return USER_LOCAL.get();
    }

    public static void removeUser(){
        USER_LOCAL.remove();
    }
}
