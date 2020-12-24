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
     * 最长空闲时间（秒）
     */
    @Value("${server.maxIdleTime:60}")
    private int maxIdleTime;
    /**
     * 连接建立后，等待CLR的最长时间（秒），客户端超过该时间没有发送CLR，则断连。
     */
    @Value("${server.auth.maxWaitTime:60}")
    private int authMaxWaitTime;
    /**
     * 连续解码异常的最大次数，超过该次数，则断连。
     */
    @Value("${server.decode.maxFailureTimes:3}")
    private int maxDecodeFailureTimes;
    /**
     * 连续发送DWR的次数，客户端超过该次数没有回DWA，则断连。
     */
    @Value("${server.dwr.times:3}")
    private int maxDwrTimes;
    //连接超时
    @Value("${service.connectTimeout:10000}")
    private int serviceConnectTimeout;
    //消息超时
    @Value("${service.readTimeout:30000}")
    private int serviceReadTimeout;
    
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
    
    @Value("${websocket.server.path:/}")
    private String websocketPath;
    
    @Value("${websocket.server.maxContentLength:65535}")
    private int websocketMaxContentLength;

    /**
     * Construct a new <code>GatewayConfig</code> instance.
     */
    public ServerConfig() {
    }
 
    /**
     * @return the maxIdleTime
     */
    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    /**
     * @return the authMaxWaitTime
     */
    public int getAuthMaxWaitTime() {
        return authMaxWaitTime;
    }

    /**
     * @return the maxDecodeFailureTimes
     */
    public int getMaxDecodeFailureTimes() {
        return maxDecodeFailureTimes;
    }

    /**
     * @return the maxDwrTimes
     */
    public int getMaxDwrTimes() {
        return maxDwrTimes;
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

    /**
     * @return the websocketPath
     */
    public String getWebsocketPath() {
        return websocketPath;
    }

    /**
     * @return the websocketMaxContentLength
     */
    public int getWebsocketMaxContentLength() {
        return websocketMaxContentLength;
    }

    /**
     * @return the serviceConnectTimeout
     */
    public int getServiceConnectTimeout() {
        return serviceConnectTimeout;
    }

    /**
     * @param serviceConnectTimeout
     *            the serviceConnectTimeout to set
     */
    public void setServiceConnectTimeout(int serviceConnectTimeout) {
        this.serviceConnectTimeout = serviceConnectTimeout;
    }

    /**
     * @return the serviceReadTimeout
     */
    public int getServiceReadTimeout() {
        return serviceReadTimeout;
    }

    /**
     * @param serviceReadTimeout
     *            the serviceReadTimeout to set
     */
    public void setServiceReadTimeout(int serviceReadTimeout) {
        this.serviceReadTimeout = serviceReadTimeout;
    }

}
