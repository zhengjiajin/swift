/**
 * @(#)ObjectMapperFactory.java 0.0.1-SNAPSHOT May 20, 2016 8:14:29 PM 
 *  
 * Copyright (c) 2015-2016 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.util.text;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapper工厂类
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date May 20, 2016 8:14:29 PM
 */
public class ObjectMapperFactory {

	private final static ObjectMapper OM = new ObjectMapper();

	static {
		OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OM.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		OM.setSerializationInclusion(Include.NON_NULL);
	}

	public static ObjectMapper getObjectMapper() {
		return OM;
	}
}
