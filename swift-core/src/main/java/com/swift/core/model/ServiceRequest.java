/*
 * @(#)ServerRequest.java   1.0  2015年8月5日
 *
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swift.core.model.data.AbstractBeanDataModel;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.model.data.core.DataModelDeserializer;
import com.swift.core.session.AbstractSession;
import com.swift.util.math.RandomUtil;
import com.swift.util.type.JsonUtil;

/**
 * 公用服务请求输入
 *
 * @author zhengjiajin
 * @version 1.0 2015年8月5日
 */
public class ServiceRequest {
    @JsonIgnore
    public static String DEFAULT_VERSION = "0";
    /**
     * 每次请求的唯一编码
     */
    private String reqId = RandomUtil.createReqId();
    /**
     * 客户端IP地址/请求对方的地址域名/系统名称等
     */
    @JsonIgnore
    private String domain;
    /**
     * 要请求的业务能力给你请求接口
     */
    private String method;
    /**
     * 接口版本号
     */
    @JsonIgnore
    private String interfaceVersion = DEFAULT_VERSION;
    /**
     * 请求时间
     */
    private Long requestTime;
    /**
     * SESSION用户对象,登录态
     */
    @JsonIgnore
    private AbstractSession sessionUser;
    /**
     * 请求业务字段
     * SimpleDataModel extends HashMap
     * data里可以嵌套String,List,Map等数据结构,也支持Map里再嵌套多层Map或List
     */
    @JsonDeserialize(using = DataModelDeserializer.class)
    private DataModel data = new MapDataModel();
    
    
    public <T extends DataModel> T getValidatorData(Class<T> cls) {
        return AbstractBeanDataModel.mapToBean(data, cls);
    }
    
    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the interfaceVersion
     */
    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    /**
     * @param interfaceVersion the interfaceVersion to set
     */
    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    public DataModel getData() {
        return data;
    }

    /**
     * SimpleDataModel
     *
     * @return
     */
    @JsonIgnore
    public DataModel getSimpleData() {
        return data;
    }

    /**
     * @return the requestTime
     */
    public Long getRequestTime() {
        return requestTime;
    }

    /**
     * @param requestTime the requestTime to set
     */
    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    /**
     * 返回前端的数据json
     */
    public String toString() {
        try {
            return JsonUtil.toJson(this);
        } catch (Exception ex) {
            throw new RuntimeException("对象转JSON出错", ex);
        }
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public AbstractSession getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(AbstractSession sessionUser) {
        this.sessionUser = sessionUser;
    }

    public static void main(String[] args) throws Exception {
        String a = " {\"appId\":\"adfds\",\"appType\":2,\"channelId\":\"\", \"method\":\"appGetOfficeList\",\"tokenId\":\"\",\"data\":{} } ";
        System.out.println(JsonUtil.toObj(a, ServiceRequest.class));
    }
}
