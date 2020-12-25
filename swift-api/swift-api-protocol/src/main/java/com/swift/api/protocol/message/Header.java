/**
 * Header.java 1.0.0 2020年12月24日
 *
 * Copyright (c) 2006-2018 GuangZhou Leopard Techonlogy Co. Ltd.  All rights reserved.
 */
package com.swift.api.protocol.message;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 消息头。
 * 
 * @version 1.0.0
 * @date 2020年12月24日
 */
public class Header {

    /**
     * 协议标识
     */
    public static final byte[] PROTOCOL_BYTES = new byte[] { 'H', 'S', 'P' };
    /**
     * 协议名称
     */
    public static final String PROTOCOL_NAME = new String(PROTOCOL_BYTES);
    /**
     * 协议版本
     */
    public static final byte VERSION = 0x01;
    /**
     * 消息长度占用的字节数
     */
    public static final int LENGTH_SIZE = 3;
    /**
     * 消息类型占用的字节数
     */
    public static final int TYPE_SIZE = 1;
    /**
     * 协议标识占用的字节数
     */
    public static final int PROTOCOL_SIZE = 3;
    /**
     * 协议版本占用的字节数
     */
    public static final int VERSION_SIZE = 1;
    /**
     * 消息头占用的字数
     */
    public static final int SIZE = LENGTH_SIZE + TYPE_SIZE + PROTOCOL_SIZE + VERSION_SIZE;

    /**
     * 消息长度
     */
    private int length = 0;
    /**
     * 消息类型
     */
    private MessageType type;
    /**
     * 协议名称
     */
    private String protocol = PROTOCOL_NAME;
    /**
     * 协议版本
     */
    private byte version = VERSION;

    /**
     * 消息头占用的字节数。
     * 
     * @return 消息头的字节数
     */
    public static int size() {
        return SIZE;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
