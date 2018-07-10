/**
 * @(#)KafkaRequest.java 0.0.1-SNAPSHOT Mar 6, 2017 12:10:31 PM 
 *  
 * Copyright (c) 2015-2017 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.dao.kafka;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.DataModelDeserializer;
import com.swift.core.model.data.MapDataModel;
import com.swift.util.text.JsonUtil;


/**
 * 业务类
 * 
 * @author
 * @version 0.0.1-SNAPSHOT
 * @date Mar 6, 2017 12:10:31 PM
 */
public class KafkaRequest {
	/**
	 * 能力名称
	 */
	private String method;

	
    /**
     * 请求业务字段
     * SimpleDataModel extends HashMap
     * data里可以嵌套String,List,Map等数据结构,也支持Map里再嵌套多层Map或List
     * 
     */
    @JsonDeserialize(using = DataModelDeserializer.class)
    private DataModel data = new MapDataModel();

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the data
	 */
	public DataModel getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(DataModel data) {
		this.data = data;
	}
	
    /**
     * 返回前端的数据json
     */
	@Override
    public String toString(){
    	try {
            return JsonUtil.toJson(this);
        } catch (Exception ex) {
            throw new RuntimeException("对象转JSON出错",ex);
        }
    }
}
