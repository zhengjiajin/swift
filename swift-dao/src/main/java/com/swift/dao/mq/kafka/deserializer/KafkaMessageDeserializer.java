/**
 * @(#)KafkaMessageDeserializer.java 0.0.1-SNAPSHOT Mar 6, 2017 2:22:06 PM 
 *  
 * Copyright (c) 2015-2017 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.dao.mq.kafka.deserializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.dao.mq.kafka.KafkaRequest;
import com.swift.util.io.ByteUtil;
import com.swift.util.text.JsonUtil;

/**
 * Kafka请求反序列化器。
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Mar 6, 2017 2:22:06 PM
 */
public class KafkaMessageDeserializer implements Deserializer<KafkaRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageDeserializer.class);

	/**
	 * @see org.apache.kafka.common.serialization.Deserializer#configure(java.util.Map,
	 *      boolean)
	 */
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	/**
	 * @see org.apache.kafka.common.serialization.Deserializer#deserialize(java.lang.String,
	 *      byte[])
	 */
	@Override
	public KafkaRequest deserialize(String topic, byte[] data) {
		try {
			if (data != null && data.length > 0) {
				 return JsonUtil.toObj(data, KafkaRequest.class);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected exception when deserialize KafkaRequest: {}", ByteUtil.bytesToHex(data), ex);
		}
		return null;
	}

	/**
	 * @see org.apache.kafka.common.serialization.Deserializer#close()
	 */
	@Override
	public void close() {

	}
}
