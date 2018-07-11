/*
 * @(#)AliMqRequest.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.alimq;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.DataModelDeserializer;
import com.swift.core.model.data.MapDataModel;
import com.swift.util.math.RandomUtil;
import com.swift.util.text.JsonUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
public class AliMqRequest {
    
    private String msgId=RandomUtil.createReqId();
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

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

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
