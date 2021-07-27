/*
 * @(#)DubboMessageDeliver.java   1.0  2021年7月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.dubbo.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.core.service.processor.AbstractMessageDeliver;
import com.swift.core.service.processor.send.MessageSender;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月14日
 */
@Component
public class DubboMessageDeliver extends AbstractMessageDeliver {

    @Autowired
    private DubboMessageSender dubboMessageSender;
    /** 
     * @see com.swift.core.service.processor.AbstractMessageDeliver#getMessageSender()
     */
    @Override
    public MessageSender getMessageSender() {
        return dubboMessageSender;
    }

}
