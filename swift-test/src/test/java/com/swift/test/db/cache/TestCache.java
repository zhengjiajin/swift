/*
 * @(#)TestCache.java   1.0  2018年7月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.test.BaseJunit4Test;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年7月6日
 */
public class TestCache extends BaseJunit4Test {

    @Autowired
    private CacheTestImpl cacheTestImpl;

    @Test
    public void test() {
        try {
            System.out.println(cacheTestImpl.cachecccdd("111"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            System.out.println(cacheTestImpl.cachecccdd("111"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            System.out.println(cacheTestImpl.cachecccdd("aaa"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            System.out.println(cacheTestImpl.cachecccdd("aaa"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
