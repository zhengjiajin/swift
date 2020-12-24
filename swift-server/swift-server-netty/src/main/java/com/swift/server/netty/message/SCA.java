/*
 * @(#)SCA.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.core.DataModelDeserializer;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
public class SCA extends MessageResponse {

    @JsonDeserialize(using = DataModelDeserializer.class)
    private DataModel data;

    /**
     * Construct a new <code>SCA</code> instance.
     */
    public SCA() {
        super();
    }

    /**
     * Construct a new <code>SCA</code> instance with request.
     */
    public SCA(final SCR request) {
        super(request);
    }

    /**
     * @see com.swift.hhmk.gateway.protocol.keepalive.message.Message#getType()
     */
    @Override
    public MessageType getType() {
        return MessageType.SCA;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

}
