/*
 * @(#)JpaBeanConfig.java   1.0  2018年6月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.jpa;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;

import com.swift.core.env.Env;
import com.swift.core.env.EnvLoader;
import com.swift.dao.db.datasource.DataSourceFactory;
import com.swift.dao.db.jpa.core.JpaBaseImpl;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月5日
 */
@Configuration
@EnableJpaRepositories(value = { "com.swift" }, includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class)},repositoryBaseClass = JpaBaseImpl.class)
public class JpaBeanConfig {
    //private static final Logger log = LoggerFactory.getLogger(JpaBeanConfig.class);

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSourceFactory dataSource) {
        if(!dataSource.isStart()) return null;
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("com.swift");
        bean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        bean.setJpaDialect(new HibernateJpaDialect());
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        if (!Env.PROD.equals(EnvLoader.getEnv())) {
            jpaVendorAdapter.setShowSql(true);
        }
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        return bean;
    }
}
