/**
 * @(#)KafkaMessageSerializer.java 0.0.1-SNAPSHOT Mar 6, 2017 2:27:42 PM 
 *  
 * Copyright (c) 2015-2017 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.dao.mq.kafka.deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.dao.mq.kafka.KafkaRequest;
import com.swift.util.text.JsonUtil;

/**
 * Kafka消息序列化器。
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Mar 6, 2017 2:27:42 PM
 */
public class KafkaMessageSerializer implements Serializer<KafkaRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageSerializer.class);

	/**
	 * @see org.apache.kafka.common.serialization.Serializer#configure(java.util.Map,
	 *      boolean)
	 */
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	/**
	 * @see org.apache.kafka.common.serialization.Serializer#serialize(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public byte[] serialize(String topic, KafkaRequest request) {
		try {
			if (request != null) {
				return JsonUtil.toJson(request).getBytes(StandardCharsets.UTF_8);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected exception when serialize message: {}", request, ex);
		}
		return null;
	}

	/**
	 * @see org.apache.kafka.common.serialization.Serializer#close()
	 */
	@Override
	public void close() {
	}
}
