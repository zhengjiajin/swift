/*
 * @(#)RocketMqConfigkey.java   1.0  2020年11月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.swift.core.spring.Spring;
import com.swift.util.type.TypeUtil;
import com.swift.util.type.UrlUtil;

/**
 * 启动配置,暂不支持事务消息
 * 
 * @author zhengjiajin
 * @version 1.0 2020年11月20日
 */
@Component
public class RocketMqConfig {
    
    public static final String default_rocket_key="default";
    
    public RocketMqConfig() {
        //开源版使用slf4j
        //System.setProperty("rocketmq.client.logUseSlf4j",logUseSlf4j);
        System.setProperty("rocketmq.client.logRoot","log/");
        System.setProperty("rocketmq.client.logLevel","warn");
        //ClientLogger
        /*ons.client.logRoot：日志保存路径。
        ons.client.logFileMaxIndex：保存历史日志文件的最大个数。
        ons.client.logLevel：日志级别。*/
        System.setProperty("ons.client.logRoot","log/");
        System.setProperty("ons.client.logFileMaxIndex","10");
        System.setProperty("ons.client.logLevel","warn");
    }
    
    private Map<String,MqConfig> ROCKET_CONFIG = new HashMap<>();
    
    private static final String rocket_key_flag="rocket.";
    
    @Value("${rocket.groupId:"+MqConfig.producerGroup+"}")
    private String def_groupId;
    
    @PostConstruct
    private void init() {
        //rocket.aliMq
        Set<String> keySet = Spring.getProperties().stringPropertyNames();
        for(String key:keySet) {
            //判断KEY
            if(!key.startsWith(rocket_key_flag)) continue;
            String rocketKey = key.substring(rocket_key_flag.length());
            String rocketValue = Spring.getProperties().getProperty(key);
            Map<String, String> rocketParm = UrlUtil.URLRequest(rocketValue);
            MqConfig config = new MqConfig();
            config.setONSAddr(UrlUtil.urlPage(rocketValue));
            config.setAccessKey(rocketParm.get("accessKey"));
            config.setSecretKey(rocketParm.get("secretKey"));
            config.setTopicTop(rocketParm.get("topicTop"));
            String groupId = TypeUtil.toString(rocketParm.get("groupId"), def_groupId);
            config.setGroupId(groupId);
            if(config.isStart()) {
                ROCKET_CONFIG.put(rocketKey, config);
            }
        }
        
    }
    
    public Map<String,MqConfig> getConfig(){
        return ROCKET_CONFIG;
    }
    
    public MqConfig getConfig(String rocketKey){
        return ROCKET_CONFIG.get(rocketKey);
    }
    
    public MqConfig getDefaultConfig() {
        return ROCKET_CONFIG.get(default_rocket_key);
    }
    
}
