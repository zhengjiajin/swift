/*
 * @(#)PathUtil.java   1.0  2020年8月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.exec;

import java.io.File;

import com.swift.util.type.TypeUtil;

/**
 * 跟径上的工具类 
 * @author zhengjiajin
 * @version 1.0 2020年8月4日
 */
public class PathUtil {
    
    public static String findOnClassPath(String filePath) {
        if(TypeUtil.isNull(filePath)) return null;
        String classpath = PathUtil.class.getResource("/").getPath();
        if(filePath.startsWith("/")) {
            filePath=filePath.substring(1);
        }
        return onPath(classpath, filePath);
    }
    
    private static String onPath(String classpath,String filePath) {
        if(TypeUtil.isNull(classpath)) return null;
        //往上找/
        String webapp = classpath;
        if(webapp.length()==webapp.lastIndexOf("/")+1) {
            webapp = classpath+filePath;
        }else {
            webapp = classpath+"/"+filePath;
        }
        if(new File(webapp).exists()) return webapp;
        if(classpath.indexOf("/")==-1) return null;
        classpath=classpath.substring(0,classpath.lastIndexOf("/"));
        return onPath(classpath,filePath);
    }
    
}
