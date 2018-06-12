/*
 * @(#)DataSource.java   1.0  2018年5月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年5月4日
 */
public enum DataSource {
    master("master"),slave("slave"),report("report");
    
    private String dbName;
    
    private DataSource(String dbName) {
        this.dbName=dbName;
    }
    
    public String getDbName() {
        return this.dbName;
    }
}
