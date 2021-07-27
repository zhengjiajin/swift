/*
 * @(#)ProviderConfiguration.java   1.0  2020年9月16日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.dubbo;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2020年9月16日
 */
@Configuration
public class ProviderConfiguration {

    @Autowired(required = false)
    private ApplicationConfig applicationConfig;

    @Autowired(required = false)
    private RegistryConfig registryConfig;

    private int TIME_OUT=1000*30;
    
    @Bean
    public ProtocolConfig protocolConfig() {
        if(applicationConfig==null) return null;
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(-1);// 随机端口
        protocolConfig.setKeepAlive(true);
        //最大可接受连接数 ,0=>不限制
        protocolConfig.setAccepts(0);
        //派发业务消息也在IO线程池其它在IO线程池，业务线程池还在统一业务线程池内
        //所有消息都不派发到线程池，全部在 IO 线程上直接执行
        protocolConfig.setDispatcher("direct");
        protocolConfig.setSerialization("fastjson");
        return protocolConfig;
    }
    
    @Bean //可选的默认配置
    public ProviderConfig providerConfig(@Autowired(required = false) ProtocolConfig protocolConfig) {
        if(applicationConfig==null) return null;
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(TIME_OUT);
        providerConfig.setAsync(true);
        providerConfig.setProtocol(protocolConfig);
        providerConfig.setRegistry(registryConfig);
        return providerConfig;
    }
   
}
