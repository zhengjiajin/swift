/**
 * @(#)HspDecoder.java 0.0.1-SNAPSHOT Dec 7, 2015 4:03:18 PM 
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.server.netty.hsp.handler;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.exception.extend.ProtocolException;
import com.swift.server.netty.NettyChannel;
import com.swift.server.netty.message.CLA;
import com.swift.server.netty.message.CLR;
import com.swift.server.netty.message.DPA;
import com.swift.server.netty.message.DPR;
import com.swift.server.netty.message.DWA;
import com.swift.server.netty.message.DWR;
import com.swift.server.netty.message.Header;
import com.swift.server.netty.message.Message;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.SCA;
import com.swift.server.netty.message.SCR;
import com.swift.util.type.ByteUtil;
import com.swift.util.type.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * HSP协议解码器
 * 
 * @date Dec 7, 2015 4:03:18 PM
 */
public class HspDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(HspDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Wait until the length prefix is available.
        if (in.readableBytes() < Header.LENGTH_SIZE) {
            return;
        }

        in.markReaderIndex();
        // Wait until the whole data is available.
        int length = in.readUnsignedMedium();
        if (in.readableBytes() + Header.LENGTH_SIZE < length) {
            in.resetReaderIndex();
            return;
        }

        if (logger.isDebugEnabled()) {
            byte[] bytes = new byte[in.readableBytes()];
            in.markReaderIndex();
            in.readBytes(bytes);
            in.resetReaderIndex();
            logger.info("Received a message from {} (HEX): {}", NettyChannel.remoteAddress(ctx.channel()),
                    ByteUtil.bytesToHex(bytes));
        }

        // Message type
        MessageType type = MessageType.fromValue(in.readByte());
        // Protocol name
        byte[] protocol = new byte[Header.PROTOCOL_SIZE];
        in.readBytes(protocol);
        if (!Arrays.equals(protocol, Header.PROTOCOL_BYTES)) {
            throw new ProtocolException("Invalid protocol: " + new String(protocol));
        }
        // Protocol version
        byte version = in.readByte();
        if (version > Header.VERSION) {
            throw new ProtocolException("Unsupported protocol version: " + version);
        }
        // Message body
        byte[] bytes = new byte[length - Header.size()];
        in.readBytes(bytes);
        String json = new String(bytes);
        logger.info("Request MSG: " + json);

        Message message = null;
        switch (type) {
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
        }
        Header header = new Header();
        header.setLength(length);
        header.setType(type);
        header.setVersion(version);
        message.setHeader(header);
        out.add(message);
    }
}
