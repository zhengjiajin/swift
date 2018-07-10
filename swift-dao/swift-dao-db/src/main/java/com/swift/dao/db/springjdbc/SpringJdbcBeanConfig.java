/*
 * @(#)SpringJdbcBeanConfig.java   1.0  2018年6月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.springjdbc;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月4日
 */
@Configuration
public class SpringJdbcBeanConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate bean = new JdbcTemplate();
        bean.setDataSource(dataSource);
        return bean;
    }
}
