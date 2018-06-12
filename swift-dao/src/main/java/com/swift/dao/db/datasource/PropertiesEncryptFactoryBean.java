/*
 * @(#)PropertiesEncryptFactoryBean.java   1.0  2016年9月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

import com.swift.util.security.DesUtil;

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
            String newUsername = deEncryptUsername(originalUsername);  
            properties.put("user", newUsername);  
        }  
        if (originalPassword != null){  
            String newPassword = deEncryptPassword(originalPassword);  
            properties.put("password", newPassword);  
        }  
    }  
      
    private String deEncryptUsername(String originalUsername){  
        return deEncryptString(originalUsername);  
    }  
      
    private String deEncryptPassword(String originalPassword){  
        return deEncryptString(originalPassword);  
    }  
    //简单加密  
    private String deEncryptString(String originalString) {
        try {
            return DesUtil.decrypt(originalString, "sYHN3d2f");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }  
}
