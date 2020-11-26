/*
 * @(#)RocketMqMessageListener.java   1.0  2020年11月25日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer;

import org.apache.rocketmq.common.message.MessageExt;

import com.swift.stream.rocketmq.pojo.MqRequest;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月25日
 */
public interface RocketMqMessageListener {
    /*
     * 全量接收,结合checkExpression使用
     */
    public boolean requestAll();
    /*
     * 收到MESSAGE后它转换成框架对象
     */
    public MqRequest changeRequest(MessageExt message);
    /*
     * 接收消息处理
     */
    public void handle(MqRequest request);
}
