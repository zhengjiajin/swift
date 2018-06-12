/*
 * @(#)SqlBuilder.java   1.0  2018年6月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.springjdbc;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月6日
 */
public class SqlBuilder {
    
    public static String removeSelect(String sql) {
        if (StringUtils.isBlank(sql)) {
            return sql;
        }
        int from = sql.toLowerCase().indexOf(" from ");
        if (from == -1) {
            return sql;
        }
        return sql.substring(from);
    }
    
    public static String toCountSql(String sql) {
        int union = sql.toLowerCase().indexOf(" union ");
        if (union != -1) {
            return "select count(tableTagwsz.*) from ("+sql+") tableTagwsz" ;
        }
        return "select count(*) " + removeSelect(sql);
    }
    

    public static String appendLimitSql(String sql, int start, int size) {
        if(sql.toLowerCase().indexOf(" limit ")!=-1) return sql;
        return sql + " LIMIT " + start + "," + size + ";";
    }
}
