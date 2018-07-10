/*
 * @(#)Jdbc.java   1.0  2018年6月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.springjdbc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月6日
 */
public interface Jdbc {
    
    boolean exist(String sql);

    public int update(String sql);
    
    public long insertForLastId(String sql);
    
    public int[] batchUpdate(String[] sqls);
    
    public <T> Page<T> queryForPage(String sql, Class<T> elementType, int pageNo, int pageSize);

    public <T> T query(String sql, Class<T> elementType);
    
    public List<Map<String, Object>> queryForMaps(String sql);
    
    public <T> List<T> queryForList(String sql, Class<T> elementType);
    
    public <T> List<T> queryForList(String sql, Class<T> elementType, int start, int size);
    
    public List<Long> queryForLongs(String sql);
    
    public List<Long> queryForLongs(String sql, int start, int size);
    
    public List<Integer> queryForInts(String sql);

    public List<Integer> queryForInts(String sql, int start, int size);
    
    public List<String> queryForStrings(String sql);

    public List<String> queryForStrings(String sql, int start, int size);
    
    public Long queryForLong(String sql);
    
    public Integer queryForInt(String sql);
    
    public String queryForString(String sql);
    
    public Date queryForDate(String sql);
}
