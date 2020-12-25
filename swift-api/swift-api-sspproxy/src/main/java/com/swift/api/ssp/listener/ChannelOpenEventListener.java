/*
 * @(#)ChannelOpenEventListener.java   1.0  2020年12月24日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.ssp.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.ClientInfo;
import com.swift.api.protocol.listener.ChannelEventListener;
import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.api.ssp.SspproxyConfig;
import com.swift.util.math.RandomUtil;

/**
 * 连接打开时需要处理监权
 * @author zhengjiajin
 * @version 1.0 2020年12月24日
 */
@Component
public class ChannelOpenEventListener implements ChannelEventListener {

    //private static final Logger logger = LoggerFactory.getLogger(ChannelOpenEventListener.class);
    @Autowired
    private SspproxyConfig sspproxyConfig;
    
    /** 
     * @see com.swift.api.protocol.listener.ChannelEventListener#channelEvent(com.swift.api.protocol.listener.ChannelEventListener.Event, com.swift.api.protocol.message.handler.MessageHandlerContext)
     */
    @Override
    public void channelEvent(Event event, MessageHandlerContext context) {
        if(!event.equals(Event.OPEN)) return;
        //发送clr
        CLR clr = new CLR();
        clr.setReqId(RandomUtil.createReqId());
        clr.setClientInfo(ClientInfo.SERVER);
        clr.setClientObject(sspproxyConfig.getSysId());
        clr.setSecSocketAccept(sspproxyConfig.getGatewayPwd());
        context.write(clr);
    }

}
