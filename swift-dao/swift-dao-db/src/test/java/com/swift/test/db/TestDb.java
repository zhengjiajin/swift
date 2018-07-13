/*
 * @(#)TestDb.java   1.0  2018年6月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.swift.core.spring.Spring;
import com.swift.dao.db.springjdbc.MarkObject;
import com.swift.test.BaseJunit4Test;
import com.swift.test.db.jdbc.SystemEnvJdbcImpl;
import com.swift.test.db.jpa.SystemEnvRepository;
import com.swift.test.db.mybatis.SystemEnvMapper;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月5日
 */
public class TestDb extends BaseJunit4Test{
    @Autowired(required=false)
    private SystemEnvRepository systemEnvRepository;
    
    @Autowired
    private SystemEnvMapper systemEnvMapper;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private SystemEnvServiceImpl systemEnvServiceImpl;
    @Autowired
    private SystemEnvJdbcImpl systemEnvJdbcImpl;
    
    
    public static void main(String[] args) {
        SystemEnvRepository aa = Spring.getBean(SystemEnvRepository.class);
        SystemEnv s = new SystemEnv();
        s.setKeyId("jpa.test");
        s.setValueContent("test");
        aa.save(s);
        System.out.println(aa.findOne("jpa.test"));
    }
    
    @Test
    @Transactional
    public void testJpa() {
        System.out.println(systemEnvRepository.findOne("jpa.test"));
        systemEnvRepository.delete("jpa.test");
        System.out.println(systemEnvRepository.findOne("jpa.test"));
        SystemEnv s = new SystemEnv();
        s.setKeyId("jpa.test");
        s.setValueContent("test");
        systemEnvServiceImpl.save(s);
        systemEnvJdbcImpl.add(s);
        systemEnvMapper.insert(s);
        System.out.println(systemEnvRepository.findOne("jpa.test"));
    }
    
    @Test
    public void testMybatis() {
        System.out.println(systemEnvMapper.get("alipay.appid"));
    }
    
    @Test
    public void testJdbc() {
        System.out.println( jdbcTemplate.queryForObject("select * from system_env where key_id='alipay.appid'", MarkObject.mark(SystemEnv.class)));
    }
    
}
