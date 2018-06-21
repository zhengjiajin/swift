/*
 * @(#)WebServer.java 2014-5-16
 * 
 * Copyright (c)	2014-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.server.jetty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.swift.exception.UnknownException;
import com.swift.server.jetty.handler.WebHandler;
import com.swift.server.jetty.util.WebContextPathUtil;

/**
 * web http服务
 * @author jiajin
 * @date 2014-5-16
 */
@Service("webServer")
public class WebServer implements LifeCycle{
	private final static Logger log = LoggerFactory.getLogger(WebServer.class);
	/**
	 * 服务
	 */
	private static Map<Integer,Server> servers = new HashMap<Integer,Server>();
	
	/**
	 * 是否重设复盖地址
	 */
	private boolean reuseAddress = true;
    
	/**
	 * 处理类
	 */
	@Autowired
    private WebHandler[] handlers = new WebHandler[0];
	
	@Autowired
	private NetworkTrafficListener networkTrafficListener;
    
	public void start() {
	    setServer();
        setHandlers();
        setContextPath();
        for(Server server:servers.values()){
            for(Connector connector : getServerPort(server)){
                log.info("连接器正在启动，端口为" + connector);
            }
            server.getHandler();
            try {
                server.start();
                for(Connector connector : getServerPort(server)){
                    log.info("连接器已启动:" + connector);
                }
            } catch (Exception e) {
                throw new UnknownException("服务启动异常", e);
            }
        }
	}
	
	private Connector[] getServerPort(Server server) {
	    return server.getConnectors();
	}
	
	private HandlerCollection getHandlerCollection(Server server){
	    return (HandlerCollection)server.getHandler();
	}
	
	
	/**
	 * 需要启动的服务器
	 */
	private void setServer(){
	    for(Integer port:getAllPort()){
	        log.info("准备启动："+port);
            Server server = new Server(new QueuedThreadPool(200));
            server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", Integer.MAX_VALUE);
            server.setConnectors(new Connector[] { httpConnector(server,port)});
            HandlerCollection handlerCollection = new HandlerCollection();
            server.setHandler(handlerCollection);
            servers.put(port, server);
        }
	}
	
	protected Connector httpConnector(Server server,Integer port){
	    NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server);
        connector.setReuseAddress(false);
        connector.setPort(port);
        connector.setReuseAddress(reuseAddress);
        connector.addNetworkTrafficListener(networkTrafficListener);
        return connector;
	}
	
	/**
	 * 设置每个SERVER里的HANDLER
	 */
	private void setHandlers() {
	    for(WebHandler handler:handlers) {
            if(handler.isPutHandler() && handler.port()>=0) {
                if(handler.port()==0){
                    for(Server server:servers.values()) {
                        getHandlerCollection(server).addHandler(handler);
                    }
                }else if(handler.port()>0){
                    getHandlerCollection(servers.get(handler.port())).addHandler(handler);
                }
            }
        }
	}
	
	private void setContextPath(){
	    for(WebHandler handler:handlers) {
	        if(handler.isPutHandler()) {
	            WebContextPathUtil.setContextPath(handler.getContextPath());
	        }
	    }
	}
	
	public void stop() {
		log.info("连接器正在停止");
		if(servers==null) return;
		for(Server server:servers.values()) {
		    try {
                server.stop();
            } catch (Exception e) {
               
            }
		}
		log.info("连接器已停止");
	}
	
	/**
	 * 找出所有非0的端口
	 * @return
	 */
	private List<Integer> getAllPort(){
	    List<Integer> ports = new ArrayList<Integer>();
	    for(WebHandler handler:handlers) {
            if(handler.isPutHandler() && handler.port()>0) {
                if(!ports.contains(handler.port()))
                ports.add(handler.port());
            }
        }
	    return ports;
	}
	
	
}
