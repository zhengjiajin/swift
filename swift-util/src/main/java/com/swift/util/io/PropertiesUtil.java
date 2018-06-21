/*
 * @(#)PropertiesUtil.java   1.0  2018年6月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.io;

import java.util.Properties;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月21日
 */
public class PropertiesUtil {
    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
            return properties;
        } catch (Exception e) {
            return null;
        }
    }
}
