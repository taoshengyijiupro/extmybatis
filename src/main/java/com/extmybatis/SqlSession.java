package com.extmybatis;

import com.extmybatis.entity.User;
import com.extmybatis.proxy.MyInvocationHandlerMbatis;

import java.lang.reflect.Proxy;

public class SqlSession {

    public static <T> T getMapper(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass},
                new MyInvocationHandlerMbatis(tClass));
    }

    public static void main(String[] args) {
        UserMapper mapper = SqlSession.getMapper(UserMapper.class);
        int insertUser = mapper.insert( "涛声依旧",22);
        System.out.println("影响行数:" + insertUser);
        User user = mapper.selectUser("涛声依旧", 22);
        System.out.println("查询结果:" + user.getUserName() + "," + user.getAge());
    }
}
