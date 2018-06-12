/*
 * @(#)TestContextLoader.java   1.0  2018年1月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.test;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextLoader;

import com.swift.core.spring.Spring;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月2日
 */
public class TestContextLoader implements ContextLoader {

    /** 
     * @see org.springframework.test.context.ContextLoader#processLocations(java.lang.Class, java.lang.String[])
     */
    @Override
    public String[] processLocations(Class<?> clazz, String... locations) {
        return locations;
    }

    /** 
     * @see org.springframework.test.context.ContextLoader#loadContext(java.lang.String[])
     */
    @Override
    public ApplicationContext loadContext(String... locations) throws Exception {
        return Spring.getApplicationContext();
    }

}
