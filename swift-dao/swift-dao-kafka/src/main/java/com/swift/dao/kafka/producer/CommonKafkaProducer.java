/**
 * @(#)IOTKafkaProducer.java 0.0.1-SNAPSHOT Mar 6, 2017 12:15:15 PM 
 *  
 * Copyright (c) 2015-2017 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.dao.kafka.producer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.swift.dao.kafka.KafkaConfigurer;
import com.swift.dao.kafka.KafkaRequest;
import com.swift.dao.kafka.KafkaService;
import com.swift.dao.kafka.deserializer.KafkaMessageSerializer;
import com.swift.util.type.TypeUtil;

/**
 * Kafka生产者客户端。
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Mar 6, 2017 12:15:15 PM
 */
@Component
public class CommonKafkaProducer implements Closeable, KafkaService {

    private final static Logger log = LoggerFactory.getLogger(CommonKafkaProducer.class);

    @Value("${classpath:kafka.properties}")
    private Resource resource;

    Producer<String, KafkaRequest> producer;

    @PostConstruct
    public void init() throws Exception {
        if (!resource.exists() || !resource.isReadable()) {
            return;
        }
        producer = new KafkaProducer<String, KafkaRequest>(getProperties());
    }

    /**
     * 不需要回调进行发送请求
     */
    public void sendRequest(String topic, KafkaRequest request) {
        Assert.notNull(request, "'request' must not be null");
        if (producer != null) {
            ProducerRecord<String, KafkaRequest> record = new ProducerRecord<String, KafkaRequest>(
                KafkaConfigurer.removeTopic(topic), request);
            log.info("=== Kafka 发送消息开始  === 请求:" + record.toString());
            producer.send(record, new SendCallback(record));
        }
    }

    /**
     * 需要回调的发送请求
     */
    public void sendRequest(String topic, KafkaRequest request, ProducerCallBack callback) {
        Assert.notNull(request, "'request' must not be null");
        if (producer != null) {
            ProducerRecord<String, KafkaRequest> record = new ProducerRecord<String, KafkaRequest>(
                KafkaConfigurer.removeTopic(topic), request);
            log.info("=== Kafka 发送消息开始  === 请求:" + record.toString());
            producer.send(record, new SendCallback(record, callback));
        }
    }

    @Override
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

    protected Properties getProperties() throws IOException {
        // 加载kafka.properties
        Properties kafkaProperties = KafkaConfigurer.getKafkaProperties();
        Properties props = new Properties();
        // 设置接入点，请通过控制台获取对应Topic的接入点
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProperty("bootstrap.servers"));
        if (TypeUtil.isNotNull(kafkaProperties.getProperty("ssl.truststore.location"))) {
            // 设置SSL根证书的路径，请记得将XXX修改为自己的路径
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                kafkaProperties.getProperty("ssl.truststore.location"));
            // 根证书store的密码，保持不变
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "KafkaOnsClient");
            // 接入协议，目前支持使用SASL_SSL协议接入
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            // SASL鉴权方式，保持不变
            props.put(SaslConfigs.SASL_MECHANISM, "ONS");
        }
        // 请求的最长等待时间
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30 * 1000);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageSerializer.class.getName());
        return props;
    }

    /**
     * producer回调
     */
    private class SendCallback implements Callback {
        ProducerRecord<String, KafkaRequest> record = null;
        ProducerCallBack callback = null;

        public SendCallback(ProducerRecord<String, KafkaRequest> record) {
            this.record = record;
        }

        public SendCallback(ProducerRecord<String, KafkaRequest> record, ProducerCallBack callback) {
            this.record = record;
            this.callback = callback;
        }

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            // send success
            if (null == e) {
                String meta = "topic:" + recordMetadata.topic() + ", partition:" + recordMetadata.partition()
                    + ", offset:" + recordMetadata.offset();
                log.info("=== Kafka 发送消息成功  ===, 请求:" + record.toString() + ", 结果:" + meta);
                if (null != callback) callback.onCompletion(recordMetadata, ProducerCallBack.SUCCESS);
                return;
            }
            // send failed
            log.error("=== Kafka 发送消息失败  ===,请求:" + record.toString() + ", 错误信息:" + e.getMessage());
            if (null != callback) callback.onCompletion(recordMetadata, ProducerCallBack.ERROR);
        }
    }

}
