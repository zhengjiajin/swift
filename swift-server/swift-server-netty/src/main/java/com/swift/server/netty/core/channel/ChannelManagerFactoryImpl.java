/**
 * @(#)ChannelManager.java 2020年12月21日
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.server.netty.core.channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.swift.server.netty.auth.AuthClient;
import com.swift.server.netty.auth.ClientInfo;
import com.swift.server.netty.core.ChannelManagerFactory;
import com.swift.server.netty.message.handler.MessageHandlerContext;
import com.swift.util.exec.ThreadUtil;

/**
 * 
 * 管理已认证的连接及上下文本
 * @author zhengjiajin
 * @version 1.0 2020年12月21日
 */
@Component
public class ChannelManagerFactoryImpl implements ChannelManagerFactory {

	private Map<AuthClient, MessageHandlerContext> channels = new ConcurrentHashMap<>();

	private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
	private static final Lock READ_LOCK = LOCK.readLock();
	private static final Lock WRITE_LOCK = LOCK.writeLock();

	/**
	 * 定时清理失效的CHANNEL
	 */
	@PostConstruct
	private void doClean() {
	   Thread doClean = new Thread() {
	        @Override
	        public void run() {
	            while(true) {
	                try {
	                    for(AuthClient client:channels.keySet()) {
	                        getChannel(client);
	                    }
	                } finally {
                        ThreadUtil.sleep(30*1000);
                    }
	            }
	        }
	    };
	    doClean.setDaemon(true);
	    doClean.start();
	}
	
	public void registerChannel(AuthClient authClient, MessageHandlerContext context) {
		WRITE_LOCK.lock();
		try {
			if (authClient == null || context == null) {
				return;
			}
			channels.put(authClient, context);
		} finally {
			WRITE_LOCK.unlock();
		}
	}

	public MessageHandlerContext getChannel(AuthClient authClient) {
		READ_LOCK.lock();
		try {
			if (authClient != null) {
			    if(!channels.containsKey(authClient)) return null;
			    MessageHandlerContext channel = channels.get(authClient);
			    if(!isActivity(channel)) {
			        channels.remove(authClient);
			        return null;
			    }
				return channel;
			}
			return null;
		} finally {
			READ_LOCK.unlock();
		}
	}
	
	private boolean isActivity(MessageHandlerContext channel) {
	    if(channel==null) return false;
	    if(channel.getChannelHandlerContext()==null) return false;
	    if(channel.getChannelHandlerContext().channel()==null) return false;
	    if(!channel.getChannelHandlerContext().channel().isActive()) return false;
	    return true;
	}

	public void deregisterChannel(AuthClient authClient) {
		WRITE_LOCK.lock();
		try {
			if (authClient != null) {
				channels.remove(authClient);
			}
		} finally {
			WRITE_LOCK.unlock();
		}
	}

    /** 
     * @see com.swift.server.netty.core.ChannelManagerFactory#registerChannel(java.lang.String, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public void registerChannel(String sysId, MessageHandlerContext context) {
        // TODO Auto-generated method stub
        
    }

    /** 
     * @see com.swift.server.netty.core.ChannelManagerFactory#registerChannel(java.lang.Object, com.swift.server.netty.auth.ClientInfo, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public void registerChannel(Object userId, ClientInfo clientInfo, MessageHandlerContext context) {
        // TODO Auto-generated method stub
        
    }

    /** 
     * @see com.swift.server.netty.core.ChannelManagerFactory#deregisterChannel(java.lang.String)
     */
    @Override
    public void deregisterChannel(String sysId) {
        // TODO Auto-generated method stub
        
    }

    /** 
     * @see com.swift.server.netty.core.ChannelManagerFactory#deregisterChannel(java.lang.Object, com.swift.server.netty.auth.ClientInfo)
     */
    @Override
    public void deregisterChannel(Object userId, ClientInfo clientInfo) {
        // TODO Auto-generated method stub
        
    }

    /** 
     * @see com.swift.server.netty.core.ChannelManagerFactory#getChannel(java.lang.String)
     */
    @Override
    public MessageHandlerContext getChannel(String sysId) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * @see com.swift.server.netty.core.ChannelManagerFactory#getChannel(java.lang.Object, com.swift.server.netty.auth.ClientInfo)
     */
    @Override
    public MessageHandlerContext getChannel(Object userId, ClientInfo clientInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * @see com.swift.server.netty.core.ChannelManagerFactory#getChannel(java.lang.Object)
     */
    @Override
    public List<MessageHandlerContext> getChannel(Object userId) {
        // TODO Auto-generated method stub
        return null;
    }
}
