/*
 * @(#)MybatisDao.java   1.0  2018年6月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.mybatis;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月6日
 */
public class MybatisDao extends SqlSessionDaoSupport{
    /**
     * 获取相关的数据库连接
     */
    public Connection getConnection() {
        return getSqlSession().getConnection();
    }
    
    public SqlSession getSqlSession() {
        return getSqlSession();
    }
    
    public <T> int delete(String _mybitsId, T obj) {
        return getSqlSession().delete(_mybitsId, obj);
    }

    public <T> int insert(String _mybitsId, T obj) {
        return getSqlSession().insert(_mybitsId, obj);
    }

    public <T> int update(String _mybitsId, T obj) {
        return getSqlSession().update(_mybitsId, obj);
    }

    public <T> List<T> query(String _mybitsId, Map<String, Object> _params) {
        return getSqlSession().selectList(_mybitsId, _params);
    }

    public <T> List<T> query(String _mybitsId, Object _params) {
        return getSqlSession().selectList(_mybitsId, _params);
    }

    public <T> T queryOne(String _mybitsId, Object object) {
        return getSqlSession().selectOne(_mybitsId, object);
    }

}
