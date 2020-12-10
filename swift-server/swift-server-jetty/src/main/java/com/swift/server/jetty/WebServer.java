/*
 * @(#)WebServer.java 2014-5-16
 * 
 * Copyright (c)	2014-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.server.jetty;

import org.eclipse.jetty.io.NetworkTrafficListener;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swift.core.server.LifeCycle;
import com.swift.exception.extend.UnknownException;
import com.swift.server.jetty.handler.WebHandler;
import com.swift.server.jetty.handler.impl.rest.WebContextPathUtil;

/**
 * web http服务
 * @author jiajin
 * @date 2014-5-16
 */
@Service("webServer")
public class WebServer implements LifeCycle{
	private final static Logger log = LoggerFactory.getLogger(WebServer.class);
	
	/**
	 * 是否重设复盖地址
	 */
	private boolean reuseAddress = true;
    
	
	private Server server;
	/**
	 * 处理类
	 */
	@Autowired
    private WebHandler[] handlers = new WebHandler[0];
	
	@Autowired
	private NetworkTrafficListener networkTrafficListener;
	
	private static int SERVER_PORT;
    
	
	public static int getServerPort() {
        return SERVER_PORT;
    }

    public void start(int port) {
	    log.info("准备启动："+port);
	    if(port<=0) {
	        log.info("不启动："+port);
	        return;
	    }
	    WebServer.SERVER_PORT=port;
        server = new Server(new QueuedThreadPool(200));
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", Integer.MAX_VALUE);
        server.setConnectors(new Connector[] { httpConnector(server,port)});
        HandlerCollection handlerCollection = new HandlerCollection();
        for(WebHandler handler:handlers) {
            handlerCollection.addHandler(handler);
        }
        server.setHandler(handlerCollection);
        setContextPath();
        try {
            server.start();
            for(Connector connector : getServerPort(server)){
                log.info("连接器已启动:" + connector);
            }
        } catch (Exception e) {
            throw new UnknownException("服务启动异常", e);
        }
	}
	
	private Connector[] getServerPort(Server server) {
	    return server.getConnectors();
	}
	
	
	protected Connector httpConnector(Server server,Integer port){
	    NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server);
        connector.setReuseAddress(false);
        connector.setPort(port);
        connector.setReuseAddress(reuseAddress);
        connector.addNetworkTrafficListener(networkTrafficListener);
        return connector;
	}

	
	private void setContextPath(){
	    for(WebHandler handler:handlers) {
	        WebContextPathUtil.setContextPath(handler.getContextPath());
	    }
	}
	
	public void stop() {
		log.info("连接器正在停止");
		if(server==null) return;
	    try {
            server.stop();
        } catch (Exception e) {
           
        }
		log.info("连接器已停止");
	}

    /** 
     * @see com.swift.core.server.LifeCycle#isStarted()
     */
    @Override
    public boolean isStarted() {
        return server.isStarted();
    }
	
}
