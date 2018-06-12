/*
 * @(#)WebchatResponse.java   1.0  2015年8月23日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.protocol.otherapp.model;

import com.swift.core.model.data.AbstractBeanDataModel;

/**
 * 微信事件业务返回类
 * 
 * @author zhengjiajin
 * @version 1.0 2015年8月23日
 */
public class WebchatResponse extends AbstractBeanDataModel {
    private String toUserName;
    
    private String fromUserName;
    
    private String content;
    /**
     * @return the toUserName
     */
    public String getToUserName() {
        return toUserName;
    }



    /**
     * @param toUserName the toUserName to set
     */
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }



    /**
     * @return the fromUserName
     */
    public String getFromUserName() {
        return fromUserName;
    }



    /**
     * @param fromUserName the fromUserName to set
     */
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }



    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }



    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }



    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[");
        sb.append(toUserName);
        sb.append("]]></ToUserName>");
        sb.append("<FromUserName><![CDATA[");
        sb.append(fromUserName);
        sb.append("]]></FromUserName>");
        sb.append("<CreateTime>");
        sb.append(System.currentTimeMillis());
        sb.append("</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA[");
        sb.append(content);
        sb.append("]]></Content>");
        sb.append("</xml>");
        return sb.toString();
    }
}
