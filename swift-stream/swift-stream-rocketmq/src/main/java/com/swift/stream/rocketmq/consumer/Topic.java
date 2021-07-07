/*
 * @(#)Topic.java   1.0  2020年11月25日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月25日
 */
@Target(value = { ElementType.TYPE })
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Topic {
    public String rocketKey() default "default";
    //topic //TODO 暂时不支持不同rocketKey相同topic
    public String[] value();
    //tag
    public String[] tag() default "*";
    //MessageModel  CLUSTERING 集群，BROADCASTING 广播
    public String messageModel() default "CLUSTERING";
}
