/**
 * KeepAliveServerHandler.java 1.0.0 2018-10-16 01:28:08
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd. All rights reserved.
 */
package com.swift.server.netty.message.handler;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.exception.ServiceException;
import com.swift.exception.extend.ProtocolException;
import com.swift.exception.extend.SystemException;
import com.swift.server.netty.Constants;
import com.swift.server.netty.NettyChannel;
import com.swift.server.netty.ServerConfig;
import com.swift.server.netty.listener.ChannelEventListener;
import com.swift.server.netty.listener.ChannelEventListener.Event;
import com.swift.server.netty.message.CLA;
import com.swift.server.netty.message.CLR;
import com.swift.server.netty.message.DPA;
import com.swift.server.netty.message.DPR;
import com.swift.server.netty.message.DWA;
import com.swift.server.netty.message.DWR;
import com.swift.server.netty.message.Message;
import com.swift.server.netty.message.MessageRequest;
import com.swift.server.netty.message.MessageResponse;
import com.swift.server.netty.message.SCA;
import com.swift.server.netty.message.SCR;
import com.swift.util.math.RandomUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 服务处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-16 01:28:08
 */
public class DefaultServerHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServerHandler.class);

    private final MessageHandlerContext CONTEXT = new MessageHandlerContext();

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private EventExecutorGroup eventExecutorGroup;
    @Autowired
    private MessageHandlerFactory messageHandlerFactory;
    
    @Autowired(required = false)
    private List<ChannelEventListener> channelEventListeners;

    /**
     * 连续解码错误次数
     */
    private AtomicInteger decodeFailures = new AtomicInteger(0);

    /*
     * (non-Javadoc)
     * 
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel. ChannelHandlerContext)
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Connected from {}", NettyChannel.remoteAddress(ctx.channel()));
        CONTEXT.setScheduledFuture(eventExecutorGroup.schedule(new Runnable() {

            @Override
            public void run() {
                logger.warn("No authentication in {} seconds, connection close: {}", serverConfig.getAuthMaxWaitTime(),
                        NettyChannel.remoteAddress(ctx.channel()));
                ctx.close();
            }
        }, serverConfig.getAuthMaxWaitTime(), TimeUnit.SECONDS));

        CONTEXT.setChannelHandlerContext(ctx);
        fireChannelEvent(Event.OPEN, CONTEXT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty. channel.ChannelHandlerContext)
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Disconnected from {}", NettyChannel.remoteAddress(ctx.channel()));
        fireChannelEvent(Event.CLOSE, CONTEXT);
        synchronized (CONTEXT.getChannelHandlerContext()) {
            CONTEXT.getChannelHandlerContext().notifyAll();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty. channel.ChannelHandlerContext,
     * java.lang.Object)
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }

        IdleStateEvent e = (IdleStateEvent) evt;
        if (e.state() == IdleState.READER_IDLE) {
            sendDWR(ctx);
            logger.warn("No message received from {} more then {} seconds, connection close",
            NettyChannel.remoteAddress(ctx.channel()), serverConfig.getMaxIdleTime());
            ctx.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty. channel.ChannelHandlerContext,
     * java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof DecoderException) {
            if (decodeFailures.incrementAndGet() > serverConfig.getMaxDecodeFailureTimes()) {
                logger.error("Failed to decode message more then {} times, connection close: {}",
                    serverConfig.getMaxDecodeFailureTimes(), NettyChannel.remoteAddress(ctx.channel()));
                ctx.close();
                return;
            }
        }
        logger.warn("An exception occurred from: {}", NettyChannel.remoteAddress(ctx.channel()), cause);
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel. ChannelHandlerContext,
     * java.lang.Object)
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        decodeFailures.set(0);
        if (CONTEXT.getUnconfirmedDwrTimes() > 0) {
            fireChannelEvent(Event.GREEN, CONTEXT);
            CONTEXT.resetUnconfirmedDwrTimes();
        }
        
        try {
            // Handler
            MessageHandler<Message> handler = messageHandlerFactory.getMessageHandler(msg.getType());
            if (handler == null) {
                throw new SystemException("No handler found for message type '" + msg.getType().name() + "'");
            }
            handler.handle(msg, CONTEXT);
            
        } catch (Throwable ex) {
            logger.error("Unexpected exception ", ex);
            if (msg instanceof MessageRequest) {
                sendError(ctx, (MessageRequest)msg, ex);
            }
        }
        
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    private void sendDWR(ChannelHandlerContext ctx) {
        if (serverConfig.getMaxDwrTimes() > 0 && CONTEXT.getUnconfirmedDwrTimes() >= serverConfig.getMaxDwrTimes()) {
            logger.warn("No DWA receive more then {} times from {}, connection close", serverConfig.getMaxDwrTimes(),
                    NettyChannel.remoteAddress(ctx.channel()));
            ctx.close();
            return;
        }

        if (CONTEXT.getUnconfirmedDwrTimes() == 1) {
            fireChannelEvent(Event.WARNING, CONTEXT);
        }

        DWR dwr = new DWR();
        dwr.setReqId(RandomUtil.createReqId());
        try {
            sendMessage(ctx, dwr);
            CONTEXT.incrementAndGetUnconfirmedDwrTimes();
        } catch (Throwable ex) {
            logger.error("Unexpected exception when sending DWR to {}: {}", NettyChannel.remoteAddress(ctx.channel()),
                    dwr, ex);
        }
    }

    private void sendError(ChannelHandlerContext ctx, MessageRequest request, Throwable cause) {
        int resultCode = Constants.INTERNAL_SERVER_ERROR;
        String reason = "Internal server error";
        if (cause instanceof ServiceException) {
            resultCode = ((ServiceException) cause).getStatusCode();
            reason = cause.getMessage();
        } 
        MessageResponse response = createResponse(request);
        response.setResultCode(resultCode);
        response.setReason(reason);
        sendMessage(ctx, response);
    }

    private MessageResponse createResponse(MessageRequest request) {
        MessageResponse response = null;
        switch (request.getType()) {
        case CLR:
            response = new CLA((CLR) request);
            break;
        case DPR:
            response = new DPA((DPR) request);
            break;
        case DWR:
            response = new DWA((DWR) request);
            break;
        case SCR:
            response = new SCA((SCR) request);
            break;
        default:
            throw new ProtocolException("Unrecognized request: " + request);
        }
        response.setReqId(request.getReqId());
        return response;
    }

    private void sendMessage(final ChannelHandlerContext ctx, final Message msg) {
        sendMessage(ctx, msg, false);
    }

    private void sendMessage(final ChannelHandlerContext ctx, final Message msg, final boolean close) {
        ctx.writeAndFlush(msg).addListener(new MessageLoggingListener(msg, close));
    }

    private void fireChannelEvent(Event event, MessageHandlerContext context) {
        if (channelEventListeners == null || channelEventListeners.isEmpty()) {
            return;
        }

        for (ChannelEventListener listener : channelEventListeners) {
            try {
                listener.channelEvent(event, context);
            } catch (Throwable ex) {
                logger.error("Unexpected exception while fire channel event: {} on listener: {}", event,
                        listener.getClass().getName(), ex);
            }
        }
    }
}
