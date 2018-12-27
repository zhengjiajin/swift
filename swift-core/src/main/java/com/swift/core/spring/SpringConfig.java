/*
 * @(#)SpringPropertyConfig.java   1.0  2018年12月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring;

import java.io.IOException;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.swift.core.env.EnvLoader;
import com.swift.exception.SwiftRuntimeException;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年12月26日
 */
@Configuration
@Order(1)
public class SpringConfig {

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer propertyLoad = new PropertyPlaceholderConfigurer();
        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = loader.getResources("classpath*:"+EnvLoader.getEnvPath()+"*.properties");
            propertyLoad.setLocations(resources);
        } catch (IOException e) {
            throw new SwiftRuntimeException("加载配置文件失败", e);
        }
        propertyLoad.setIgnoreResourceNotFound(true);
        propertyLoad.setIgnoreUnresolvablePlaceholders(true);
        return propertyLoad;
    }
    
    
}
