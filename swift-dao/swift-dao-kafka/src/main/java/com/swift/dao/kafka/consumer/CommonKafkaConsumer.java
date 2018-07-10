/**
 * @(#)IOTKafkaConsumer.java 0.0.1-SNAPSHOT Mar 6, 2017 2:33:11 PM 
 *  
 * Copyright (c) 2015-2017 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.dao.kafka.consumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.swift.dao.kafka.KafkaConfigurer;
import com.swift.dao.kafka.KafkaRequest;
import com.swift.dao.kafka.KafkaService.KafkaMessageHandler;
import com.swift.dao.kafka.KafkaService.Topic;
import com.swift.dao.kafka.deserializer.KafkaMessageDeserializer;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.type.TypeUtil;

/**
 * Kafka消息者。
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Mar 6, 2017 2:33:11 PM
 */
@Component
public class CommonKafkaConsumer extends Thread implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	@Value("${classpath:kafka.properties}")
	private Resource resource;
	@Autowired(required = false)
	private List<KafkaMessageHandler> handlers;

	private KafkaConsumer<String, KafkaRequest> consumer;

	private AtomicBoolean isTerminated = new AtomicBoolean(false);

	@PostConstruct
	public void init() throws Exception {
		if (!resource.exists() || !resource.isReadable()) {
			return;
		}
		if(handlers==null ||handlers.isEmpty()) return;
		ArrayList<String> subscribeTopics = new ArrayList<String>();
		for(KafkaMessageHandler handler:handlers) {
		    Topic topic = AnnotationUtil.getAnnotation(handler.getClass(), Topic.class);
            if(topic==null) continue;
            for(String tp:topic.value()) {
                tp = KafkaConfigurer.removeTopic(tp);
                if(!subscribeTopics.contains(tp)) {
                    subscribeTopics.add(tp);
                }
            }
		}
		if(subscribeTopics.isEmpty()) return;
		consumer = new KafkaConsumer<String, KafkaRequest>(getProperties());
		consumer.subscribe(subscribeTopics);
		setName(getClass().getSimpleName());
		setDaemon(true);
		start();
	}

	@PreDestroy
	@Override
	public void close() throws IOException {
		isTerminated.set(true);
		try {
			if (isAlive()) {
				join();
			}
		} catch (Throwable ex) {
		}
	}

	@Override
	public void run() {
		while (!isTerminated.get()) {
			ConsumerRecords<String, KafkaRequest> records = consumer.poll(Long.MAX_VALUE);
			consumeMessage(records);
		}
		if (consumer != null) {
			consumer.close();
		}
	}

	private void consumeMessage(ConsumerRecords<String, KafkaRequest> records) {
		try {
			for (TopicPartition partition : records.partitions()) {
				List<ConsumerRecord<String, KafkaRequest>> partitionRecords = records.records(partition);
				for (ConsumerRecord<String, KafkaRequest> record : partitionRecords) {
					handle(record);
				}
				long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
				consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected message when consume kafka records: {}", records, ex);
		}
	}

	protected Properties getProperties() throws IOException {
	    //加载kafka.properties
        Properties kafkaProperties =  KafkaConfigurer.getKafkaProperties();
        Properties props = new Properties();
        //设置接入点，请通过控制台获取对应Topic的接入点
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProperty("bootstrap.servers"));
        //与sasl路径类似，该文件也不能被打包到jar中
        if (TypeUtil.isNotNull(kafkaProperties.getProperty("ssl.truststore.location"))) {
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaProperties.getProperty("ssl.truststore.location"));
            //根证书store的密码，保持不变
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "KafkaOnsClient");
            //接入协议，目前支持使用SASL_SSL协议接入
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            //SASL鉴权方式，保持不变
            props.put(SaslConfigs.SASL_MECHANISM, "ONS");
        }
        //两次poll之间的最大允许间隔
        //请不要改得太大，服务器会掐掉空闲连接，不要超过30000
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 25000);
        //每次poll的最大数量
        //注意该值不要改得太大，如果poll太多数据，而不能在下次poll之前消费完，则会触发一次负载均衡，产生卡顿
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 30);
        //当前消费实例所属的消费组，请在控制台申请之后填写
        //属于同一个组的消费实例，会负载消费消息
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getProperty("group.id"));
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageDeserializer.class.getName());
		return props;
	}

	private void handle(ConsumerRecord<String, KafkaRequest> record) {
	    LOGGER.info("收到KAFKA消息:"+record.topic()+";"+record.value());
		if (handlers == null || handlers.isEmpty()) {
			return;
		}
		for (KafkaMessageHandler handler : handlers) {
		    Topic topic = AnnotationUtil.getAnnotation(handler.getClass(), Topic.class);
		    if(topic==null) continue;
		    if(!TypeUtil.inList(topic.value(), KafkaConfigurer.localTopic(record.topic())))  continue;
		    try {
		        handler.handle(record.value());
		    } catch (Throwable ex) {
	            LOGGER.error(handler+" error ", ex);
	        }
		}
	}
}
