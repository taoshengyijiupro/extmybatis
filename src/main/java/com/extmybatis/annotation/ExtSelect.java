package com.extmybatis.annotation;


import java.lang.annotation.*;

/**
 * 自定义查询注解z
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface   ExtSelect {
    String value();
}
