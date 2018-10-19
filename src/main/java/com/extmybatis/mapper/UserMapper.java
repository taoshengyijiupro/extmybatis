package com.extmybatis.mapper;


import com.extmybatis.entity.User;
import com.extmybatis.annotation.ExtInsert;
import com.extmybatis.annotation.ExtParam;
import com.extmybatis.annotation.ExtSelect;

/**
 * 纯手写mybatis 注解
 */
public interface UserMapper {

    @ExtInsert("insert into user(username,age) values(#{userName},#{age})")
    int insert(@ExtParam("userName") String userName, @ExtParam("age") Integer age);

    @ExtSelect("select username AS userName,age from user where username = #{userName} and age = #{age}")
    User selectUser(@ExtParam("userName") String userName, @ExtParam("age") Integer age);
}
