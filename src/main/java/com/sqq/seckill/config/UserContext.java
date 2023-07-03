package com.sqq.seckill.config;

import com.sqq.seckill.pojo.User;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/7 11:04
 */
public class UserContext {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    public static void setUser(User user){
        userThreadLocal.set(user);

    }

    public static User getUser(){
        return userThreadLocal.get();
    }

}
