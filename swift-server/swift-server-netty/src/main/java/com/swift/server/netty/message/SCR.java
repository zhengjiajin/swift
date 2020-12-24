/*
 * @(#)SCR.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.model.data.core.DataModelDeserializer;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
public class SCR extends MessageRequest {

    private String interfaceVersion;
    private String method;
    @JsonDeserialize(using = DataModelDeserializer.class)
    private DataModel data=new MapDataModel();
   

    /**
     * @see com.swift.hhmk.gateway.protocol.keepalive.message.Message#getType()
     */
    @Override
    public MessageType getType() {
        return MessageType.SCR;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

}
