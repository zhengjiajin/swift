/*
 * @(#)ServerResponse.java   1.0  2015年8月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.model.data.core.DataModelDeserializer;
import com.swift.util.type.JsonUtil;

/**
 * 业务响应。
 * @author zhengjiajin
 * @version 1.0 2015年8月5日
 */
public class ServiceResponse implements Serializable {
   
    private static final long serialVersionUID = -4387570136090783356L;
    /**
     *返回码
     */
    private int resultCode;
    /**
     * 响应结果详细描述。
     */
    private String reason;
    /**
     * 响应时间
     */
    @JsonIgnore
    private long responseTime=System.currentTimeMillis();
    /**
     * 本响应所对应的请求。
     */
    @JsonIgnore
    @JSONField(serialize=false)
    private ServiceRequest request;
    /**
     * 返回的业务字段
     */
    @JsonDeserialize(using = DataModelDeserializer.class)
    private DataModel data = new MapDataModel();
    
    
    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }
    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }
    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    /**
     * @return the data
     */
    public DataModel getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(DataModel data) {
        this.data = data;
    }
    
    /**
     * DataModel
     * @return
     */
    @JsonIgnore
    @JSONField(serialize=false)
    public DataModel getSimpleData() {
        return data;
    }
    /**
     * @param request the request to set
     */
    public void setRequest(ServiceRequest request) {
        this.request = request;
    }
    /**
     * @return the request
     */
    public ServiceRequest getRequest() {
        return request;
    }
    /**
     * @return the responseTime
     */
    public long getResponseTime() {
        return responseTime;
    }
    /**
     * @param responseTime the responseTime to set
     */
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
   
    /**
     * 返回前端的数据json
     */
    public String toString(){
        try {
            return JsonUtil.toJson(this);
        } catch (Exception ex) {
            throw new RuntimeException("对象转JSON出错",ex);
        }
    }
}
