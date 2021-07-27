/*
 * @(#)ApplicationJsonProtocol.java   1.0  2020年12月1日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.http.apache.protocol;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.http.util.ContentTypeUtil;
import com.swift.core.api.protocol.ClientProtocol;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.FileDefinition;
import com.swift.core.model.parser.HttpDomainUtil;
import com.swift.core.session.AbstractSession;
import com.swift.core.session.SessionCrypt;
import com.swift.exception.ResultCode;
import com.swift.exception.extend.SystemException;
import com.swift.util.math.RandomUtil;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 需要把业务协议转换为客户端协议 
 * @author zhengjiajin
 * @version 1.0 2020年12月1日
 */
@Component
public class ApacheApplicationJsonProtocol implements ClientProtocol<HttpUriRequest,HttpResponse> {
    
    
    @Autowired(required=false)
    private SessionCrypt sessionCrypt;
    //ContentType
    /** 
     * @see com.swift.core.api.protocol.ClientProtocol#toRequest(com.swift.core.model.ServiceRequest)
     */
    @Override
    public HttpUriRequest toRequest(ServiceRequest req) {
        HttpPost post = new HttpPost();
        post.setURI(HttpDomainUtil.toUriDamain(req));
        if(req.getSessionUser()!=null && sessionCrypt!=null) {
            post.setHeader("Cookie", AbstractSession.SESSION_NAME+"="+sessionCrypt.encrypt(req.getSessionUser()));
        }
        Map<String,FileDefinition> fileList = findFile(req.getData());
        if(TypeUtil.isNull(fileList)) {
            post.setHeader(ContentTypeUtil.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            post.setEntity(new StringEntity(JsonUtil.toJson(req.getData()), ContentType.APPLICATION_JSON));
        } else {
            String boundary = "---------"+RandomUtil.createReqId();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));
            builder.setBoundary(boundary);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            addFileParm(fileList, builder);
            addParm(req, fileList, builder);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
        }
        return post;
    }

    /**
     * @param req
     * @param fileList
     * @param builder
     */
    private void addParm(ServiceRequest req, Map<String, FileDefinition> fileList, MultipartEntityBuilder builder) {
        for(String key : req.getData().keySet()) {
            if(!fileList.containsKey(key)) {
                Object obj = req.getData().getObject(key);
                if(obj instanceof DataModel) {
                    builder.addTextBody(key, JsonUtil.toJson(obj));
                }else {
                    builder.addTextBody(key, TypeUtil.toString(obj));
                }
                
            }
        }
    }

    /**
     * @param fileList
     * @param builder
     */
    private void addFileParm(Map<String, FileDefinition> fileList, MultipartEntityBuilder builder) {
        for(String filekey : fileList.keySet()) {
            FileDefinition file = fileList.get(filekey); 
            ContentType contentType = ContentType.DEFAULT_BINARY;
            String fileName = file.getFileName();
            if(TypeUtil.isNotNull(file.getContentType())) {
                try {
                    contentType = ContentType.create(file.getContentType());
               }catch(Exception e) {
                   
               }
            }
            if(TypeUtil.isNull(fileName)) {
                fileName=RandomUtil.createReqId();
            }
            builder.addBinaryBody(filekey, file.getInputStream(), contentType, fileName);
        }
    }
    
    private Map<String,FileDefinition> findFile(DataModel data){
        if(TypeUtil.isNull(data)) return null;
        Map<String,FileDefinition> fileMap = new HashMap<>();
        for(String key : data.keySet()) {
            if(data.getObject(key) instanceof FileDefinition) {
                fileMap.put(key, (FileDefinition)data.getObject(key));
            }
        }
        if(TypeUtil.isNull(fileMap)) return null;
        return fileMap;
    }

    /** 
     * @see com.swift.core.api.protocol.ClientProtocol#toResponse(java.lang.Object)
     */
    @Override
    public ServiceResponse toResponse(ServiceRequest req,HttpResponse r) {
        if(r.getEntity()==null) throw new SystemException("接收消息异常");
        ServiceResponse response = new ServiceResponse();
        try {
            String contentType = getHeader(r, ContentTypeUtil.CONTENT_TYPE);
            if(ContentTypeUtil.isJsonText(contentType)) {
                String json = EntityUtils.toString(r.getEntity());
                response = JsonUtil.toObj(json, ServiceResponse.class);
            }
            
            if(ContentTypeUtil.isFileData(contentType)) {
                String fileDis = getHeader(r, "Content-Disposition");
                FileDefinition file = new FileDefinition();
                if(r.getEntity().getContentType()!=null) {
                    file.setContentType(r.getEntity().getContentType().getValue());
                }
                file.setFileName(TypeUtil.subStr(fileDis, "filename=\"", "\""));
               
                file.setInputStream(r.getEntity().getContent());
                file.setSize(r.getEntity().getContentLength());
                response.setData(file);
            }
            //"application/octet-stream";
            
            //Content-Disposition "attachment; filename=\"" + UrlUtil.urlEncode(fileName) + "\""
            
        } catch (Exception e) {
            response.setResultCode(ResultCode.SYS_ERROR);
            response.setReason(e.getMessage());
        }
        response.setRequest(req);
        response.setResponseTime(System.currentTimeMillis());
        return response;
    }
    
    private String getHeader(HttpMessage mess,String name) {
        Header header = mess.getFirstHeader(name);
        if(header==null) return ContentType.APPLICATION_JSON.getMimeType();
        String value = header.getValue();
        if(TypeUtil.isNull(value)) return ContentType.APPLICATION_JSON.getMimeType();
        return value;
    }
}
