/*
 * @(#)MqRequest.java   1.0  2020年11月25日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.model.data.core.DataModelDeserializer;
import com.swift.util.math.RandomUtil;
import com.swift.util.type.JsonUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2020年11月25日
 */
public class MqRequest {

    private String msgId = RandomUtil.createReqId();
    /**
     * 名称TAG
     */
    private String tag;
    /**
     * 请求业务字段 SimpleDataModel extends HashMap data里可以嵌套String,List,Map等数据结构,也支持Map里再嵌套多层Map或List
     * 接口固定参数：Request，Session，Response
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
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag
     *            the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
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
    public String toString() {
        try {
            return JsonUtil.toJson(this);
        } catch (Exception ex) {
            throw new RuntimeException("对象转JSON出错", ex);
        }
    }
}
