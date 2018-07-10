/*
 * @(#)MarkObject.java   1.0  2018年1月9日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.dao.db.springjdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月9日
 */
public class MarkObject {
    
    public static <T> BeanPropertyRowMapper<T> mark(Class<T> cls){
        BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(cls);
        return rowMapper;
    }
}
