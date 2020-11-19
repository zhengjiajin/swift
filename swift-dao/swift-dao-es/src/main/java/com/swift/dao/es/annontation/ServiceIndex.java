/*
 * @(#)ServiceIndex.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.es.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceIndex {
    
    public Tnteraction keyTnteraction() default Tnteraction.request;
    /**
     * 取主键路径
     * 
     * @return
     */
    public String primaryPath(); // 业务主键的字段 例如：关注用户，则把关注的用户在DataModel类的位置描出来，后台filter拦截时寻找

    //索引名,每个系统固定一个
    public String index();
    //表名 新版已取消此类型
    public String type() default "_doc";
    //取数的spring service
    public String updateIndexClassName();
    
    public enum Tnteraction {
        request, response
    }
}
