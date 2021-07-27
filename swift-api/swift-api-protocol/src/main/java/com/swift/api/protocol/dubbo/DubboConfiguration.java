/*
 * @(#)DubboConfiguration.java   1.0  2021年7月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.protocol.dubbo;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.Constants;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swift.core.env.Env;
import com.swift.core.env.EnvLoader;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月8日
 */
@Configuration
public class DubboConfiguration {
    @Value("${sysId:}")
    private String sysId;
    
    @Value("${dubbo.registry.address:}")
    private String address;
    
    @Value("${dubbo.registry.username:}")
    private String username;
    
    @Value("${dubbo.registry.password:}")
    private String password;
    
    @Bean // #2
    public ApplicationConfig applicationConfig() {
        if(TypeUtil.isNull(address)) return null;
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(sysId);
        applicationConfig.setQosEnable(false);//关闭在线运维
        if(EnvLoader.getEnv()==Env.DEV){
            applicationConfig.setEnvironment(Constants.TEST_ENVIRONMENT);
        }
        if(EnvLoader.getEnv()==Env.TEST){
            applicationConfig.setEnvironment(Constants.TEST_ENVIRONMENT);
        }
        if(EnvLoader.getEnv()==Env.DEMO){
            applicationConfig.setEnvironment(Constants.DEVELOPMENT_ENVIRONMENT);
        }
        if(EnvLoader.getEnv()==Env.PROD){
            applicationConfig.setEnvironment(Constants.PRODUCTION_ENVIRONMENT);
        }
        ApplicationModel.getConfigManager().setApplication(applicationConfig);
        return applicationConfig;
    }

    @Bean // #3
    public RegistryConfig registryConfig() {
        if(TypeUtil.isNull(address)) return null;
        RegistryConfig registryConfig = new RegistryConfig();
        //registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress(address);
        registryConfig.setTimeout(30000);
        registryConfig.setGroup(EnvLoader.getEnv().toString()+"_DUBBO");
        if(TypeUtil.isNotNull(password)) registryConfig.setPassword(password);
        if(TypeUtil.isNotNull(username)) registryConfig.setUsername(username);
        return registryConfig;
    }

}
