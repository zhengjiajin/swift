/*
 * @(#)MybatisBeanConfig.java   1.0  2018年6月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月4日
 */
@Configuration
public class MybatisBeanConfig {
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource,
        @Value("classpath*:mybatis/**/*.xml") Resource[] mapperLocations,
        @Value("classpath*:mybatis-config.xml") Resource configLocation) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(mapperLocations);
        bean.setConfigLocation(configLocation);
        return bean;
    }
    
    @Bean
    public MybatisDao baseDao(SqlSessionFactory sqlSessionFactory) {
        MybatisDao bean = new MybatisDao();
        bean.setSqlSessionFactory(sqlSessionFactory);
        return bean;
    }
    
    @Bean
    @Autowired
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer bean = new MapperScannerConfigurer();
        bean.setAnnotationClass(MybatisMapper.class);
        bean.setBasePackage("com.swift");
        bean.setMarkerInterface(IMybatisMapper.class);
        return bean;
    }
}
