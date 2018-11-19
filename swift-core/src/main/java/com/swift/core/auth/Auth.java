package com.swift.core.auth;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * 接口权限注解 
 * @author zhengjiajin
 * @version 1.0 2018年11月19日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Auth {
    AuthType[] type() default {AuthType.USER};
}
