/*
 * @(#)ImplServiceObject.java   1.0  2017年3月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

/**
 * 用于查找实现|继承某个类的所有类
 * classConfig 可为：classpath*:com\smarthome\**\*.class
 * @author zhengjiajin
 * @version 1.0 2017年3月4日
 */
public class ImplServiceObject extends PathMatchingResourcePatternResolver{
    private final static Logger log = LoggerFactory.getLogger(ImplServiceObject.class);
    
    private final static String CLA_NAME = ".class";
    
    private static ImplServiceObject implServiceObject = new ImplServiceObject();
    
    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    
    private ImplServiceObject(){
    }
    
    /**
     * 得到classpath里的所有类
     * @param classpath
     * @return
     */
    public static Resource[] getClasspathResources(String classpath){
        try {
            return implServiceObject.getResources(classpath);
        } catch (IOException ex) {
            log.error("",ex);
            return null;
        }
    }
    /**
     * 得到实现了某个接口的所有类
     * @param classpath
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getImplInterface(String classpath,Class<T> cla){
        List<String> claStrList = getClassName(classpath);
        List<T> beanList = new ArrayList<T>();
        if(claStrList==null || claStrList.isEmpty()) return beanList;
        for(String beanName:claStrList){
            Class<?> beanCls=null;
            try {
                beanName=beanName.replace("/", ".").substring(0, beanName.lastIndexOf(CLA_NAME));
                beanCls =  classLoader.loadClass(beanName);
                if(cla.isAssignableFrom(beanCls) && !beanCls.equals(cla)){
                    beanList.add((T)beanCls.newInstance());
                }
            } catch (Throwable ex) {
                log.error(beanName+" create error ",ex);
            }
        }
        return beanList;
    }
    /**
     * 得到所有className的名字
     * @param classpath
     * @return
     */
    public static List<String> getClassName(String classpath){
        Resource[] resource =  getClasspathResources(classpath);
        List<String> list = new ArrayList<String>();
        if(resource==null) return list;
        for(Resource res:resource){
            list.add(changeUrlToClassName(classpath, res.toString()));
        }
        return list;
    }
    /**
     * @param classpath
     * @param classUrl
     * @return
     */
    private static String changeUrlToClassName(String classpath,String classUrl){
        classUrl=StringUtils.cleanPath(classUrl);
        classpath=StringUtils.cleanPath(classpath);
        classpath= classpath.substring(classpath.indexOf(":")+1);
        classpath= classpath.substring(0, classpath.indexOf("*")==-1?classpath.length():classpath.indexOf("*"));
        return classUrl.substring(classUrl.indexOf(classpath), classUrl.lastIndexOf(CLA_NAME)+6);
    }
    
    /**
     * @param classLoader the classLoader to set
     */
    public static void setClassLoader(ClassLoader classLoader) {
        ImplServiceObject.classLoader = classLoader;
    }
}
