/*
 * @(#)ZookeeperListener.java   1.0  2018年8月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.zookeeper.annontation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月27日
 */
@Target(value= {ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ZKListener {
    /**
     * 监听的目录
     * @return
     */
    public String value() default "/";
    /**
     * 是否监听子目录
     * @return
     */
    public boolean listenerChild() default true;
    
    /**
     * 监控类型
     * @return
     */
    public Type[] type() default {Type.NODE_ADDED,Type.NODE_UPDATED,Type.NODE_REMOVED};
}
