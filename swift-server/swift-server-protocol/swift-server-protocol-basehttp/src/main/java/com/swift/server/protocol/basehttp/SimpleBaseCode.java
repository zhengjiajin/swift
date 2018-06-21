/*
 * @(#)SimpleBaseCode.java   1.0  2015年8月17日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.protocol.basehttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.swift.core.model.FileDefinition;
import com.swift.core.model.HttpServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.server.jetty.protocol.AbstractWebHandlerCode;
import com.swift.server.jetty.protocol.WebHandlerCode;
import com.swift.util.io.ByteUtil;
import com.swift.util.text.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * HHMK自定义格式的HTTP协议编解码
 * 
 * @author zhengjiajin
 * @version 1.0 2015年8月17日
 */
@Service
@Order
public class SimpleBaseCode extends AbstractWebHandlerCode implements WebHandlerCode {
    private final static Logger log = LoggerFactory.getLogger(SimpleBaseCode.class);

    private final static String CONTENT_TYPE_JSON = "text/json";
    
    private final static String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    
    private final static String SERVICE_KEY = "data";
    
    /**
     * @see com.hhmk.hospital.server.core.handler.simplecode.WebHandlerCode#decode(java.lang.String,
     *      org.eclipse.jetty.server.Request, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public HttpServiceRequest decode(String target, Request rawHttpRequest, HttpServletRequest request,HttpServletResponse response) {
            HttpServiceRequest req = new HttpServiceRequest();
            req.setIp(getIp(rawHttpRequest));
            req.setRequestTime(System.currentTimeMillis());
            req.setMethod(getMethod(target));
            req.setInterfaceVersion(getInterfaceVersion(target));
            req.setRequest(request);
            req.setResponse(response);
            DataModel data = new MapDataModel();
            String contentType = TypeUtil.toString(rawHttpRequest.getHeader("Content-Type"), CONTENT_TYPE_FORM);
            if(contentType.indexOf(CONTENT_TYPE_JSON)!=-1) {
                doJson(rawHttpRequest,data);
            }
            req.setFiles(doPost(rawHttpRequest, data));
            doGet(rawHttpRequest, data);
            req.setData(data);
            return req;
    }
    
    private void doJson(HttpServletRequest rawHttpRequest,DataModel data) {
        try {
            String json = new String(ByteUtil.getInputStream(rawHttpRequest.getInputStream()));
            if (TypeUtil.isNull(json)) return;
            DataModel jsonDataModel = JsonUtil.toObj(json, MapDataModel.class);
            for(String key : jsonDataModel.keySet()) {
                data.addObject(key, jsonDataModel.getObject(key));
            }
        }catch(Exception ex) {
            log.error("数据流出错:",ex);
        }
    }
    
    private void doGet(HttpServletRequest rawHttpRequest,DataModel data) {
        Map<String, String[]> old = rawHttpRequest.getParameterMap();
        for (Entry<String, String[]> entry : old.entrySet()) {
            if(entry.getKey().equals(SERVICE_KEY)) {
                DataModel model = JsonUtil.toObj(entry.getValue()[0], MapDataModel.class);
                for(String key : model.keySet()) {
                    data.addObject(key, model.getObject(key));
                }
            }else {
                if (entry.getValue().length > 0) {
                    for(String value:entry.getValue()){
                        data.addObject(entry.getKey(), value); 
                    }
                }
            }
        }
    }

    private List<FileDefinition> doPost(HttpServletRequest rawHttpRequest,DataModel data) {
        try {
            if (ServletFileUpload.isMultipartContent(rawHttpRequest)) {
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                List<?> list = upload.parseRequest(rawHttpRequest);
                List<FileDefinition> definitions = new ArrayList<FileDefinition>();
                for (int i = 0; i < list.size(); i++) {
                    FileItem f = (FileItem) list.get(i);
                    if (f.isFormField()) {
                        log.info("收到POST参数:" + f.getFieldName() + ":长度：" + f.getSize());
                        data.addObject(f.getFieldName(), f.getString());
                    }else {
                        log.info("收到POST文件:" + f.getName() + ":长度：" + f.getSize());
                        definitions.add(createFileDefinition(f));
                    }
                }
                return definitions;
            }
        }catch(Exception ex) {
            log.error("post解释出错",ex);
        }
        return null;
    }

    private FileDefinition createFileDefinition(FileItem f) throws IOException {
        FileDefinition def = new FileDefinition();
        def.setContentType(f.getContentType());
        def.setFileName(f.getName());
        def.setInputStream(f.getInputStream());
        def.setSize(f.getSize());
        return def;
    }
    
    /**
     * @see com.hhmk.hospital.server.core.handler.simplecode.WebHandlerCode#encode(com.ServiceResponse.hospital.server.common.model.server.ServerResponse)
     */
    @Override
    public ResModel encode(ServiceResponse res) {
        try {
            ResModel model = new ResModel();
            model.setContentType("text/json;charset=UTF-8");
            model.setStatus(200);
            String json = JsonUtil.toJson(res);
            model.setBody(json.getBytes());
            return model;
        } catch (Exception ex) {
            log.error("输出解释出错:", ex);
            throw new ServiceException(ResultCode.PROTOCOL_ERROR, "协议解释异常");
        }
    }

    /**
     * @see com.hhmk.hospital.server.core.handler.simplecode.WebHandlerCode#error(int, int)
     */
    @Override
    public ResModel error(int errorCode, String errorMsg) {
        ResModel model = new ResModel();
        model.setContentType("text/json;charset=UTF-8");
        model.setStatus(200);
        ServiceResponse res = new ServiceResponse();
        res.setResultCode(errorCode);
        res.setReason(errorMsg);
        String json = JsonUtil.toJson(res);
        log.error("错误返回：" + json);
        model.setBody(json.getBytes());
        return model;
    }

    private static final String V_STR="/V";
    
    private String getInterfaceVersion(String target){
        try{
            if(target.indexOf(V_STR)==-1) return String.valueOf(Float.MAX_VALUE);
            target = target.substring(target.indexOf(V_STR)+V_STR.length());
            String v = target.substring(0,target.indexOf("/"));
            if(TypeUtil.isNumber(v)) return v;
        }catch(Exception e){
            log.error("协议转成版本异常:"+target,e);
        }
        return String.valueOf(Float.MAX_VALUE);
    }
    
    private String getMethod(String target){
        try{
            if(target.indexOf(V_STR)!=-1) target = target.substring(target.indexOf(V_STR)+V_STR.length());
            if(target.indexOf("/")==-1) return null;
            return target.substring(target.indexOf("/")+1);
        }catch(Exception e){
            log.error("协议转成版本异常:"+target,e);
        }
        return null;
    }
    
    /**
     * @see com.hhmk.hospital.server.core.handler.simplecode.WebHandlerCode#isThisCode(java.lang.String,
     *      org.eclipse.jetty.server.Request)
     */
    @Override
    public boolean isThisCode(String target) {
        return true;
    }

    /**
     * @see com.hhmk.hospital.server.core.handler.simplecode.WebHandlerCode#setCookie(com.ServiceResponse.hospital.server.common.model.server.ServerResponse)
     */
    @Override
    public List<Cookie> setCookie(ServiceResponse res) {
        return null;
    }

}
