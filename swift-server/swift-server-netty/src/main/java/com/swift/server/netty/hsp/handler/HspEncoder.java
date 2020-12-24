/**
 * @(#)HspEncoder.java 0.0.1-SNAPSHOT Dec 8, 2015 4:41:19 PM 
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.server.netty.hsp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.server.netty.NettyChannel;
import com.swift.server.netty.message.Header;
import com.swift.server.netty.message.Message;
import com.swift.util.type.ByteUtil;
import com.swift.util.type.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * HSP协议编码器
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Dec 8, 2015 4:41:19 PM
 */
public class HspEncoder extends MessageToByteEncoder<Message> {

    private static final Logger logger = LoggerFactory.getLogger(HspEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) {
        String json = JsonUtil.toJson(msg);
        byte[] bytes = json.getBytes();
        int length = Header.size() + bytes.length;
        if (msg.getHeader() == null) {
            Header header = new Header();
            header.setType(msg.getType());
            header.setLength(length);
            msg.setHeader(header);
        }
        out.writeMedium(length);
        out.writeByte(msg.getType().value());
        out.writeBytes(Header.PROTOCOL_BYTES);
        out.writeByte(msg.getHeader().getVersion());
        out.writeBytes(bytes);
        if (logger.isDebugEnabled()) {
            out.markReaderIndex();
            byte[] encoded = new byte[out.readableBytes()];
            out.readBytes(encoded);
            out.resetReaderIndex();
            logger.info("Sending message to {} (HEX): {}", NettyChannel.remoteAddress(ctx.channel()),
                    ByteUtil.bytesToHex(encoded));
        }
    }
}
