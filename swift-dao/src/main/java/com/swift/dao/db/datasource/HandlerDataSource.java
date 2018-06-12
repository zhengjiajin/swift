/*
 * @(#)HandlerDataSource.java   1.0  2018年5月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年5月4日
 */
public class HandlerDataSource {
    
    private static ThreadLocal<String> handlerThredLocal = new ThreadLocal<String>();

    public static void putDataSource(String datasource) {
        handlerThredLocal.set(datasource);
    }

    public static String getDataSource() {
        return handlerThredLocal.get();
    }

    public static void clear() {
        handlerThredLocal.remove();
    }
}
