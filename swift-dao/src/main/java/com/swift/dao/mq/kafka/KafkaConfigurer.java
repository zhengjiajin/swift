/*
 * @(#)KafkaConfigurer.java   1.0  2018年6月7日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.mq.kafka;

import java.net.URL;
import java.util.Properties;

import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月7日
 */
public class KafkaConfigurer {

    private static Properties properties;

    static {
        // 这个路径必须是一个文件系统可读的路径，不能被打包到jar中
        URL url = KafkaConfigurer.class.getClassLoader().getResource("kafka_client_jaas.conf");
        if(url!=null) {
            String path = url.getPath();
            if(path.indexOf("/")==0) path=path.substring(1);
            System.setProperty("java.security.auth.login.config", path);
        }
    }

    public synchronized static Properties getKafkaProperties() {
        if (null != properties) {
            return properties;
        }
        // 获取配置文件kafka.properties的内容
        Properties kafkaProperties = new Properties();
        try {
            kafkaProperties.load(KafkaConfigurer.class.getClassLoader().getResourceAsStream("kafka.properties"));
        } catch (Exception e) {
            return null;
        }
        URL url = KafkaConfigurer.class.getClassLoader().getResource("kafka.client.truststore.jks");
        if(url!=null) {
            String path = url.getPath();
            if(path.indexOf("/")==0) path=path.substring(1);
            kafkaProperties.setProperty("ssl.truststore.location", path);
        }
        properties = kafkaProperties;
        return kafkaProperties;
    }

    public static String removeTopic(String topic) {
        Properties kafkaProperties = KafkaConfigurer.getKafkaProperties();
        String topicTop = kafkaProperties.getProperty("topicTop");
        if(TypeUtil.isNotNull(topicTop)) {
            if(topic.indexOf(topicTop)==-1) topic=topicTop+topic;
        }
        return topic;
    }
    
    public static String localTopic(String topic) {
        Properties kafkaProperties = KafkaConfigurer.getKafkaProperties();
        String topicTop = kafkaProperties.getProperty("topicTop");
        if(TypeUtil.isNotNull(topicTop)) {
            topic=topic.replace(topicTop, "");
        }
        return topic;
    }
}
