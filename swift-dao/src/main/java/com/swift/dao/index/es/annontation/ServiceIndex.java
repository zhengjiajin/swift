/*
 * @(#)ServiceIndex.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.swift.dao.index.es.factory.UpdateIndexService;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceIndex {
    
    /**
     * 取主键路径
     * 
     * @return
     */
    public String primaryPath(); // 业务主键的字段 例如：关注用户，则把关注的用户在DataModel类的位置描出来，后台filter拦截时寻找

    /**
     * 请求或响应或SESSION处取主键
     * 
     * @return
     */
    public Tnteraction interaction() default Tnteraction.request; // 交互方式触发保存行为日志 request:请求时保存用户的行为日志;response:返回时保存用户的行为日志
    //索引名,每个系统固定一个
    public String IndexName() default "swift";
    //表名
    public String typeKey();
    //取数的spring service
    public Class<UpdateIndexService> updateIndexClassName();
    
    public enum Tnteraction {
        request, response
    }
}
