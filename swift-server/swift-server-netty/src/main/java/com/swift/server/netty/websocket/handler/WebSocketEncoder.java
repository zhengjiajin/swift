/*
 * @(#)WebSocketEncoder.java   1.0  2018年8月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.websocket.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.server.netty.message.Message;
import com.swift.util.type.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月29日
 */
public class WebSocketEncoder extends MessageToMessageEncoder<Message>{

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEncoder.class);
    /** 
     * @see io.netty.handler.codec.MessageToMessageEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        logger.debug("WebSocketEncoder收到:"+msg);
        out.add(new TextWebSocketFrame(JsonUtil.toJson(msg)));
    }

}
