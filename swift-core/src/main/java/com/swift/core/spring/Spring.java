/*
 * @(#)Spring.java  2014-1-22
 * 
 * Copyright (c)	2014-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 使用该类保存一个JVM内全局唯一的Spring ApplicationContext，保存后就可以使用该类的静态方法获得Spring Bean。
 * 
 * 主要目的是使非ApplicationContext管理的对象（如内嵌的Web应用）能够获得Spring Bean。
 * 
 * @auther jiajin
 * @version 1.0 2014-1-22
 */
public class Spring extends DispatcherServlet{

    private static final long serialVersionUID = 7425497776707646548L;
    private static ApplicationContext applicationContext;
    private static boolean isLoading = false;
    private static Properties properties = new Properties();
    
    protected void initStrategies(ApplicationContext context) {
        super.initStrategies(context);
        if (applicationContext == null) {
            applicationContext = context;
        }
    }
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
	/**
	 * 保存ApplicationContext。
	 * 
	 * @throws RuntimeException
	 *             当企图第二次设置ApplicationContext时抛出该异常。
	 */
    @Override
	public void setApplicationContext(ApplicationContext ctx) {
		if (applicationContext == null) {
		    applicationContext = ctx;
		}
		super.setApplicationContext(ctx);
	}
	
	public static ApplicationContext getApplicationContext(){
	    synchronized ("Spring.applicationContext") {
            if (applicationContext == null) {
                String[] configLocations = {"applicationContext.xml" };
                if(!isLoading) {
                    isLoading=true;
                    applicationContext = new ClassPathXmlApplicationContext(
                            configLocations);
                }
            }
        }
	    return applicationContext;
	}

	/**
	 * 根据beanId返回spring bean。
	 */
	public static Object getBean(String beanId) {
		return getApplicationContext().getBean(beanId);
	}
	/**
     * 根据beanId返回spring bean。
     */
    public static <T> T getBean(Class<T> requiredType) {
        return getApplicationContext().getBean(requiredType);
    }
    /**
     * 根据beanId返回spring bean。
     */
    public static <T> List<T> getBeans(Class<T> requiredType) {
        Map<String,T> map = getApplicationContext().getBeansOfType(requiredType);
        if(map == null) return null;
        return new ArrayList<T>(map.values());
    }
    /**
     * 根据beanId返回spring bean。
     */
    public static <T> T getBean(String beanId,Class<T> requiredType) {
        return getApplicationContext().getBean(beanId,requiredType);
    }
    /**
     * 系统里所有BEAN
     * @return
     */
    public static String[] getAllBeanName(){
        return getApplicationContext().getBeanDefinitionNames();
    }

    public static Properties getProperties() {
        return properties;
    }
    
}
