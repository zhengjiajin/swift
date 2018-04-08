/*
 * @(#)HttpClientHandler.java   1.0  2018年1月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.dao.output.httpclient.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月19日
 */
public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        if (msg instanceof HttpResponse)   
        {  
            HttpResponse response = (HttpResponse) msg;  
            System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));  
        }  
        if(msg instanceof HttpContent)  
        {  
            HttpContent content = (HttpContent)msg;  
            ByteBuf buf = content.content();  
            System.out.println(buf.toString(CharsetUtil.UTF_8));  
            buf.release();  
        }  
    }  
}
