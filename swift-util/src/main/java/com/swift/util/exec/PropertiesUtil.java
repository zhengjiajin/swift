/*
 * @(#)PropertiesUtil.java   1.0  2018年6月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.exec;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.swift.util.type.JsonUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月21日
 */
public class PropertiesUtil {
    
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
    
    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try {
            log.info("加载资源文件:"+fileName);
            InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
            if(in!=null) {
                properties.load(in);
            }else {
                ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
                Resource resource[] = loader.getResources("classpath*:"+fileName);
                if(resource!=null) {
                    for(Resource res: resource) {
                        if(res!=null && res.getInputStream()!=null) properties.load(res.getInputStream());
                    }
                }
            }
            log.info("加载资源文件内容为:"+JsonUtil.toJson(properties));
            return properties;
        } catch (Exception e) {
            return null;
        }
    }
}
