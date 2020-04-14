/*
 * @(#)PropertiesEncryptFactoryBean.java   1.0  2016年9月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

import com.swift.core.env.EnvDecode;
import com.swift.util.security.AESUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2016年9月8日
 */
public class PropertiesEncryptFactoryBean implements FactoryBean<Properties> {

    private Properties properties;  
    /** 
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public Properties getObject() throws Exception {
        return getProperties();  
    }

    /** 
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<Properties> getObjectType() {
        return java.util.Properties.class; 
    }

    /** 
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;  
    }

    public Properties getProperties() {  
        return properties;  
    } 
    
    public void setProperties(Properties inProperties) {  
        this.properties = inProperties;  
        String originalUsername = properties.getProperty("user");  
        String originalPassword = properties.getProperty("password");  
        if (originalUsername != null){  
            String newUsername = EnvDecode.decode(originalUsername);
            properties.put("user", newUsername);  
        }  
        if (originalPassword != null){  
            String newPassword = EnvDecode.decode(originalPassword);  
            properties.put("password", newPassword);  
        }  
    }  
  
    
    public static void main(String[] args) throws Exception{
        System.out.println(AESUtil.encrypt("", ""));
        System.out.println(AESUtil.encrypt("", ""));
    }
}
