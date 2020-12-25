/**
 * GatewayConfig.java 1.0.0 2019-03-27 15:45:36
 *
 * Copyright (c) 2015-2019 GuangZhou HHMK Co. Ltd. All rights reserved.
 */
package com.swift.server.netty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置。
 * 
 * @version 1.0.0
 * @date 2019-03-27 15:45:36
 */
@Component
public class ServerConfig {

    
    /**
     * HSP监听端口号
     */
    @Value("${hsp.server.port:996}")
    private int hspServerPort;
    
    @Value("${hsp.server.backlog:1024}")
    private int hspServerBacklog;
    
    @Value("${hsp.server.tcpNoDelay:true}")
    private boolean hspServerTcpNoDelay;
    
    @Value("${hsp.server.reuseAddress:true}")
    private boolean hspServerReuseAddress;
    
    @Value("${hsp.server.keepalive:true}")
    private boolean hspServerKeepalive;
    
    /**
     * Websocket监听端口号
     */
    @Value("${websocket.server.port:997}")
    private int websocketServerPort;
    
    @Value("${websocket.server.backlog:1024}")
    private int websocketServerBacklog;
    
    @Value("${websocket.server.tcpNoDelay:true}")
    private boolean websocketServerTcpNoDelay;
    
    @Value("${websocket.server.reuseAddress:true}")
    private boolean websocketServerReuseAddress;
    
    @Value("${websocket.server.keepalive:true}")
    private boolean websocketServerKeepalive;
    
    /**
     * Construct a new <code>GatewayConfig</code> instance.
     */
    public ServerConfig() {
    }
 

    /**
     * @return the hspServerPort
     */
    public int getHspServerPort() {
        return hspServerPort;
    }

    /**
     * @return the hspServerBacklog
     */
    public int getHspServerBacklog() {
        return hspServerBacklog;
    }

    /**
     * @return the hspServerTcpNoDelay
     */
    public boolean isHspServerTcpNoDelay() {
        return hspServerTcpNoDelay;
    }

    /**
     * @return the hspServerReuseAddress
     */
    public boolean isHspServerReuseAddress() {
        return hspServerReuseAddress;
    }

    /**
     * @return the hspServerKeepalive
     */
    public boolean isHspServerKeepalive() {
        return hspServerKeepalive;
    }

    /**
     * @return the websocketServerPort
     */
    public int getWebsocketServerPort() {
        return websocketServerPort;
    }

    /**
     * @return the websocketServerBacklog
     */
    public int getWebsocketServerBacklog() {
        return websocketServerBacklog;
    }

    /**
     * @return the websocketServerTcpNoDelay
     */
    public boolean isWebsocketServerTcpNoDelay() {
        return websocketServerTcpNoDelay;
    }

    /**
     * @return the websocketServerReuseAddress
     */
    public boolean isWebsocketServerReuseAddress() {
        return websocketServerReuseAddress;
    }

    /**
     * @return the websocketServerKeepalive
     */
    public boolean isWebsocketServerKeepalive() {
        return websocketServerKeepalive;
    }

}
