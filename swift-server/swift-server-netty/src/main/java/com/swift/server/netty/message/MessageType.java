/*
 * @(#)MessageType.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
public enum MessageType {
    /**
     * CLR
     */
    CLR((byte) 0x01),
    /**
     * CLA
     */
    CLA((byte) 0x02),
    /**
     * DWR
     */
    DWR((byte) 0x03),
    /**
     * DWA
     */
    DWA((byte) 0x04),
    /**
     * SCR
     */
    SCR((byte) 0x05),
    /**
     * SCA
     */
    SCA((byte) 0x06),

    DPR((byte) 0x07),

    DPA((byte) 0x08);

    private byte value;

    private MessageType(byte value) {
        this.value = value;
    }

    @JsonValue
    public byte value() {
        return value;
    }

    @JsonCreator
    public static MessageType fromValue(byte v) {
        for (MessageType m : MessageType.values()) {
            if (m.value == v) {
                return m;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}
