/*
 * @(#)ContentTypeUtil.java   1.0  2020年12月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.http.util;

import org.apache.http.entity.ContentType;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月14日
 */
public class ContentTypeUtil {

    public static final String CONTENT_TYPE="Content-Type";
    
    public static boolean isJsonText(String contentType) {
        if(contentType.indexOf(ContentType.APPLICATION_JSON.getMimeType())!=-1) return true;
        if(contentType.indexOf(ContentType.TEXT_PLAIN.getMimeType())!=-1)  return true;
        if(contentType.indexOf("text/json")!=-1)  return true;
        return false;
    }
    
    public static boolean isFileData(String contentType) {
        if(contentType.indexOf(ContentType.APPLICATION_OCTET_STREAM.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.IMAGE_BMP.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.IMAGE_GIF.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.IMAGE_JPEG.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.IMAGE_PNG.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.IMAGE_SVG.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.IMAGE_TIFF.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.IMAGE_WEBP.getMimeType())!=-1)  return true;
        if(contentType.indexOf(ContentType.MULTIPART_FORM_DATA.getMimeType())!=-1)  return true;
        return false;
    }
}
