/*
 * @(#)DbBeanConfig.java   1.0  2018年5月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年5月4日
 */
@Configuration
//@PropertySource(value = { "classpath:jdbc.properties" })
@EnableTransactionManagement
public class DbBeanConfig {

    @Bean
    public JpaTransactionManager transactionManager(DataSource dataSource) {
        JpaTransactionManager bean = new JpaTransactionManager();
        bean.setDataSource(dataSource);
        return bean;
    }

}
