/*
 * @(#)DbBeanConfig.java   1.0  2018年5月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.ResourceTransactionManager;

import com.swift.dao.db.datasource.DataSourceFactory;

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
    public ResourceTransactionManager transactionManager(DataSourceFactory dataSource) {
        if(!dataSource.isStart()) return null;
        JpaTransactionManager bean = new JpaTransactionManager();
        bean.setDataSource(dataSource);
        return bean;
    }

}
