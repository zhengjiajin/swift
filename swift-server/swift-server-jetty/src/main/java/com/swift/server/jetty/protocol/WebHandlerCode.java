/*
 * @(#)WebHandlerCode.java   1.0  2015年8月17日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.jetty.protocol;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.swift.core.model.HttpServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.util.type.TypeUtil;

/**
 * 接收服务的编解码器
 * @author zhengjiajin
 * @version 1.0 2015年8月17日
 */
public interface WebHandlerCode {
    
    public boolean isThisCode(String target);
    
    public HttpServiceRequest decode(String target, Request rawHttpRequest, HttpServletRequest request, HttpServletResponse response);
    
    public ResModel encode(ServiceResponse res);
    
    public ResModel error(int errorCode,String errorMsg);
    
    public List<Cookie> setCookie(ServiceResponse res);
    
    public class ResModel {
        
        private String contentType;
        
        private int status = 200;
        
        private Map<String,String> header = new HashMap<String,String>();
        
        private List<Cookie> cookie = new LinkedList<Cookie>();
        
        private byte[] body;

        /**
         * @return the contentType
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * @param contentType the contentType to set
         */
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        /**
         * @return the status
         */
        public int getStatus() {
            return status;
        }

        /**
         * @param status the status to set
         */
        public void setStatus(int status) {
            this.status = status;
        }

        /**
         * @return the header
         */
        public Map<String, String> getHeader() {
            return header;
        }

        /**
         * @param header the header to set
         */
        public void setHeader(Map<String, String> header) {
            this.header = header;
        }

        /**
         * @return the cookie
         */
        public List<Cookie> getCookie() {
            return cookie;
        }

        /**
         * @param cookie the cookie to set
         */
        public void setCookie(List<Cookie> cookie) {
            this.cookie = cookie;
        }

        /**
         * @return the body
         */
        public byte[] getBody() {
            return body;
        }

        /**
         * @param body the body to set
         */
        public void setBody(byte[] body) {
            this.body = body;
        }
        
        
    }
    
    public default String getIp(HttpServletRequest httpRequest){
        String ip = httpRequest.getHeader("X-Forwarded-For");
        if(TypeUtil.isNull(ip)){
            ip=httpRequest.getRemoteAddr();
        }else{
            if(ip.indexOf(",")>0){
                ip=ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
