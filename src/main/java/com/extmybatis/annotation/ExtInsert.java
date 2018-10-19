package com.extmybatis.annotation;

import java.lang.annotation.*;

/**
 * 自定插入注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExtInsert {
    String value();
}
