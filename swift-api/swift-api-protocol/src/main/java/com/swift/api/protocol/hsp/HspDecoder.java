/**
 * @(#)HspDecoder.java 1.0 2020年12月25日
 *  
 * Copyright (c) 2015-2020 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.api.protocol.hsp;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.api.protocol.message.CLA;
import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.DPA;
import com.swift.api.protocol.message.DPR;
import com.swift.api.protocol.message.DWA;
import com.swift.api.protocol.message.DWR;
import com.swift.api.protocol.message.Header;
import com.swift.api.protocol.message.Message;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.SCA;
import com.swift.api.protocol.message.SCR;
import com.swift.api.protocol.util.NettyChannel;
import com.swift.exception.extend.ProtocolException;
import com.swift.util.type.ByteUtil;
import com.swift.util.type.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * HSP协议解码器
 * @author zhengjiajin
 * @version 1.0 2020年12月25日
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
