/*
 * @(#)ApplicationJsonProtocol.java   1.0  2020年12月1日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.http.apache.protocol;

import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.http.util.HttpDomainUtil;
import com.swift.core.api.protocol.ClientProtocol;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.FileDefinition;
import com.swift.core.model.parser.DataJsonParser;
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
    
    private static final String CONTENT_TYPE="Content-Type";
    
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
            post.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            post.setEntity(new StringEntity(JsonUtil.toJson(req.getData()), ContentType.APPLICATION_JSON));
        } else {
            post.setHeader(CONTENT_TYPE, ContentType.MULTIPART_FORM_DATA.getMimeType());
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            addFileParm(fileList, builder);
            addParm(req, fileList, builder);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
        }
        return null;
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
                    contentType=ContentType.create(file.getContentType());
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
        
        return null;
    }

    /** 
     * @see com.swift.core.api.protocol.ClientProtocol#toResponse(java.lang.Object)
     */
    @Override
    public ServiceResponse toResponse(ServiceRequest req,HttpResponse r) {
        if(r.getEntity()==null) throw new SystemException("接收消息异常");
        ServiceResponse response = new ServiceResponse();
        response.setRequest(req);
        response.setResponseTime(System.currentTimeMillis());
        try {
            String contentType = getHeader(r, "Content-Type");
            if(contentType.equals(ContentType.APPLICATION_JSON.getMimeType())) {
                String json = EntityUtils.toString(r.getEntity());
                response.setData(DataJsonParser.jsonToObject(json));
            }
            
            if(contentType.equals(ContentType.APPLICATION_OCTET_STREAM.getMimeType())) {
                String fileDis = getHeader(r, "Content-Disposition");
                FileDefinition file = new FileDefinition();
                if(r.getEntity().getContentType()!=null) {
                    file.setContentType(r.getEntity().getContentType().getValue());
                }
                file.setFileName(TypeUtil.subStr(fileDis, "filename=\"", "\""));
               
                file.setInputStream(r.getEntity().getContent());
                file.setSize(r.getEntity().getContentLength());
            }
            //"application/octet-stream";
            
            //Content-Disposition "attachment; filename=\"" + UrlUtil.urlEncode(fileName) + "\""
            
        } catch (Exception e) {
            response.setResultCode(ResultCode.SYS_ERROR);
            response.setReason(e.getMessage());
        }
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
