/**
 * @(#)HspEncoder.java 1.0 2020年12月25日
 *  
 * Copyright (c) 2015-2020 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.api.protocol.hsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.api.protocol.message.Header;
import com.swift.api.protocol.message.Message;
import com.swift.api.protocol.util.NettyChannel;
import com.swift.util.type.ByteUtil;
import com.swift.util.type.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * HSP协议编码器
 * @author zhengjiajin
 * @version 1.0 2020年12月25日
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
