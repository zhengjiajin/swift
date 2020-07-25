/*
 * @(#)SpringPropertyConfig.java   1.0  2018年12月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.swift.core.env.EnvLoader;
import com.swift.exception.extend.SystemException;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年12月26日
 */
@Configuration
@Order(1)
public class SpringConfig {

    private static final Logger log = LoggerFactory.getLogger(SpringConfig.class);
    
    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer propertyLoad = new PropertyPlaceholderConfigurer();
        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = loader.getResources("classpath*:"+EnvLoader.getEnv().getEnvPath()+"*.properties");
            if(resources!=null) {
                for(Resource re:resources) {
                    log.info("加载配置文件:"+re.getURL());
                    Spring.getProperties().load(re.getInputStream());
                }
                propertyLoad.setLocations(resources);
            }
        } catch (IOException e) {
            throw new SystemException("加载配置文件失败", e);
        }
        propertyLoad.setIgnoreResourceNotFound(true);
        propertyLoad.setIgnoreUnresolvablePlaceholders(true);
        return propertyLoad;
    }
    
    
}
