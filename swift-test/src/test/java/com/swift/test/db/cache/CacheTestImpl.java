/*
 * @(#)CacheTestImpl.java   1.0  2018年7月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db.cache;

import org.springframework.stereotype.Component;

import com.alicp.jetcache.anno.Cached;
import com.swift.exception.ServiceException;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月6日
 */
@Component
public class CacheTestImpl {
    @Cached(expire=60)
    public String cachecccdd(String abc) {
        System.out.println("abc");
        if(abc.equals("111")) throw new ServiceException(500001,"error");
        return "11111"+abc;
    }
}
