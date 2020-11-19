/*
 * @(#)SqlBuilder.java   1.0  2018年6月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.springjdbc;

import org.apache.commons.lang3.StringUtils;

import com.swift.util.type.TypeUtil;

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
        boolean selectAll = false;
        if (sql.toLowerCase().indexOf(" union ") != -1) selectAll=true;
        if (sql.toLowerCase().indexOf(" group ") != -1) selectAll=true;
        if (sql.toLowerCase().indexOf(" order ") != -1) selectAll=true;
        if(selectAll) return "select count(*) from ("+sql+") tableTagwsz" ;
        return "select count(*) " + removeSelect(sql);
    }
    

    public static String appendLimitSql(String sql, long start, int size) {
        if(sql.toLowerCase().indexOf(" limit ")!=-1) return sql;
        return sql + " LIMIT " + start + "," + size + ";";
    }
    
    public static String appendWhere(String sql,String querySql,Object value) {
        if(TypeUtil.isNull(value)) return sql;
        if(sql.toLowerCase().indexOf(" where ")!=-1) {
            return sql+" "+createQuery(querySql, value);
        }else {
            return sql + " where "+createQuery(querySql, value);
        }
    }
    
    public static String createQuery(String querySql,Object value) {
        return querySql.replace("?", String.valueOf(value));
    }
}
