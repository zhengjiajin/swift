/*
 * @(#)KafkaConfigurer.java   1.0  2018年6月7日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.alimq;

import java.util.Properties;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.swift.core.env.EnvDecode;
import com.swift.util.io.PropertiesUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月7日
 */
public class AliMqConfigurer {

    private static Properties properties;

    public synchronized static Properties getProperties() {
        if (null != properties) {
            return properties;
        }
        // 获取配置文件kafka.properties的内容
        Properties mqproperties = PropertiesUtil.getProperties("alimq.properties");
        properties = new Properties();
        // 您在 MQ 控制台创建的 Consumer ID
        if(TypeUtil.isNotNull(mqproperties.getProperty("group.ConsumerId")))
            properties.put(PropertyKeyConst.ConsumerId, mqproperties.getProperty("group.ConsumerId"));
        // 您在 MQ 控制台创建的 Producer ID
        if(TypeUtil.isNotNull(mqproperties.getProperty("group.ProducerId")))
            properties.put(PropertyKeyConst.ProducerId, mqproperties.getProperty("group.ProducerId"));
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,mqproperties.getProperty("alimq.AccessKey"));
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, EnvDecode.decode(mqproperties.getProperty("alimq.SecretKey")));
        // 设置 TCP 接入域名（此处以公共云公网环境接入为例）
        properties.put(PropertyKeyConst.ONSAddr, mqproperties.getProperty("alimq.ONSAddr"));
        
        properties.put("topicTop",  mqproperties.getProperty("topicTop"));
        return properties;
    }

    public static String removeTopic(String topic) {
        Properties mqProperties = AliMqConfigurer.getProperties();
        String topicTop = mqProperties.getProperty("topicTop");
        if(TypeUtil.isNotNull(topicTop)) {
            if(topic.indexOf(topicTop)==-1) topic=topicTop+topic;
        }
        return topic;
    }
    
    public static String localTopic(String topic) {
        Properties mqProperties = AliMqConfigurer.getProperties();
        String topicTop = mqProperties.getProperty("topicTop");
        if(TypeUtil.isNotNull(topicTop)) {
            topic=topic.replace(topicTop, "");
        }
        return topic;
    }
}
