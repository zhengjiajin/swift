/*
 * @(#)JdbcMysqlImpl.java   1.0  2018年6月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.springjdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.swift.exception.SwiftRuntimeException;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月6日
 */
public class JdbcImpl extends JdbcTemplate implements Jdbc {
    private static final Logger log = LoggerFactory.getLogger(JdbcImpl.class);

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#exist(java.lang.String)
     */
    @Override
    public boolean exist(String sql) {
        return this.queryForInt(sql) > 0;
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#insertForLastId(java.lang.String)
     */
    @Override
    public long insertForLastId(String sql) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        super.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return pstmt;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForPage(java.lang.String, java.lang.Class, int, int)
     */
    @Override
    public <T> Page<T> queryForPage(String sql, Class<T> elementType, int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo-1, pageSize, null);
        String countSql = SqlBuilder.toCountSql(sql);
        String limitSql = SqlBuilder.appendLimitSql(sql, pageable.getOffset(), pageable.getPageSize());
        int count = this.queryForInt(countSql);
        List<T> list = new ArrayList<T>();
        if (count > 0) {
            list = this.queryForList(limitSql, elementType);
        }
        Page<T> page = new PageImpl<T>(list,pageable,count);
        return page;
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#query(java.lang.String, java.lang.Class)
     */
    @Override
    public <T> T query(String sql, Class<T> elementType) {
        return super.queryForObject(sql, MarkObject.mark(elementType));
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForList(java.lang.String, java.lang.Class)
     */
    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType) {
        return super.query(sql, MarkObject.mark(elementType));
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForList(java.lang.String, java.lang.Class, int, int)
     */
    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType, int start, int size) {
        sql = SqlBuilder.appendLimitSql(sql, start, size);
        return this.queryForList(sql, elementType);
    }


    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForLongs(java.lang.String)
     */
    @Override
    public List<Long> queryForLongs(String sql) {
        List<Long> list = super.query(sql, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int index) {
                try {
                    return rs.getLong(1);
                } catch (SQLException e) {
                    throwException(e);
                }
                return null;
            }
        });
        return list;
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForLongs(java.lang.String, int, int)
     */
    @Override
    public List<Long> queryForLongs(String sql, int start, int size) {
        sql = SqlBuilder.appendLimitSql(sql, start, size);
        return this.queryForLongs(sql);
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForInts(java.lang.String)
     */
    @Override
    public List<Integer> queryForInts(String sql) {
        List<Integer> list = super.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int index) {
                try {
                    return rs.getInt(1);
                } catch (SQLException e) {
                    throwException(e);
                }
                return null;
            }
        });
        return list;
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForInts(java.lang.String, int, int)
     */
    @Override
    public List<Integer> queryForInts(String sql, int start, int size) {
        sql = SqlBuilder.appendLimitSql(sql, start, size);
        return this.queryForInts(sql);
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForStrings(java.lang.String)
     */
    @Override
    public List<String> queryForStrings(String sql) {
        List<String> list = super.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int index) {
                try {
                    return rs.getString(1);
                } catch (SQLException e) {
                    throwException(e);
                }
                return null;
            }
        });
        return list;
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForStrings(java.lang.String, int, int)
     */
    @Override
    public List<String> queryForStrings(String sql, int start, int size) {
        sql = SqlBuilder.appendLimitSql(sql, start, size);
        return this.queryForStrings(sql);
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForLong(java.lang.String)
     */
    @Override
    public Long queryForLong(String sql) {
        return super.queryForObject(sql, Long.class);
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForInt(java.lang.String)
     */
    @Override
    public Integer queryForInt(String sql) {
        return super.queryForObject(sql, Integer.class);
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForString(java.lang.String)
     */
    @Override
    public String queryForString(String sql) {
        return super.queryForObject(sql, String.class);
    }

    /**
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForDate(java.lang.String)
     */
    @Override
    public Date queryForDate(String sql) {
        return super.queryForObject(sql, Date.class);
    }

    private void throwException(SQLException e) {
        log.error("数据异常", e);
        throw new SwiftRuntimeException("数据异常", e);
    }

    /** 
     * @see com.swift.dao.db.springjdbc.Jdbc#queryForMaps(java.lang.String)
     */
    @Override
    public List<Map<String, Object>> queryForMaps(String sql) {
        return super.queryForList(sql);
    }

}
