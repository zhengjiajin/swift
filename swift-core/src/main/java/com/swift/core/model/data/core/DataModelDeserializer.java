/**
 * @(#)DataModelDeserializer.java 1.0.0 May 8, 2016 2:42:37 PM 
 *  
 * Copyright (c) 2001-2016 GuangDong Eshore Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.core.model.data.core;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;



/**
 * TODO Purpose/description of class
 * 
 * @author Avery Xiao
 * @version 1.0.0
 * @date May 8, 2016 2:42:37 PM
 */
public class DataModelDeserializer extends JsonDeserializer<DataModel> {

	@Override
	public DataModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		return jp.readValueAs(MapDataModel.class);
	}
}
