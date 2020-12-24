/*
 * @(#)WebSocketDecoder.java   1.0  2018年8月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.websocket.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.parser.DataJsonParser;
import com.swift.server.netty.message.CLA;
import com.swift.server.netty.message.CLR;
import com.swift.server.netty.message.DPA;
import com.swift.server.netty.message.DPR;
import com.swift.server.netty.message.DWA;
import com.swift.server.netty.message.DWR;
import com.swift.server.netty.message.Message;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.SCA;
import com.swift.server.netty.message.SCR;
import com.swift.util.type.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月29日
 */
public class WebSocketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketDecoder.class);
    
    /** 
     * @see io.netty.handler.codec.MessageToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        String json = msg.text();
        logger.debug("WebSocketDecoder收到:"+json);
        DataModel data = DataJsonParser.jsonToObject(json);
        Integer reqType = data.getInteger("reqType");
        MessageType messageType = MessageType.fromValue(reqType.byteValue());
        Message message = null;
        switch (messageType) {
            case CLR:
                message = JsonUtil.toObj(json, CLR.class);
                break;
            case CLA:
                message = JsonUtil.toObj(json, CLA.class);
                break;
            case DWR:
                message = JsonUtil.toObj(json, DWR.class);
                break;
            case DWA:
                message = JsonUtil.toObj(json, DWA.class);
                break;
            case SCR:
                message = JsonUtil.toObj(json, SCR.class);
                break;
            case SCA:
                message = JsonUtil.toObj(json, SCA.class);
                break;
            case DPR:
                message = JsonUtil.toObj(json, DPR.class);
                break;
            case DPA:
                message = JsonUtil.toObj(json, DPA.class);
                break;
            default:
                break;
            }
        out.add(message);
    }

}
