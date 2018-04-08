/*
 * @(#)Spring.java  2014-1-22
 * 
 * Copyright (c)	2014-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.spring;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 使用该类保存一个JVM内全局唯一的Spring ApplicationContext，保存后就可以使用该类的静态方法获得Spring Bean。
 * 
 * 主要目的是使非ApplicationContext管理的对象（如内嵌的Web应用）能够获得Spring Bean。
 * 
 * @auther jiajin
 * @version 1.0 2014-1-22
 */
public class Spring extends DispatcherServlet implements ServletContextListener {

    private static final long serialVersionUID = -1703674689007064897L;

    private static final String SPRING_IMPL_PATH = "classpath*:com/smarthome/**/*.class";

    private static ApplicationContext applicationContext;

    private static ContextLoaderListener contextLoaderListener = new ContextLoaderListener();

    private static String[] SPRING_CONFIG_XML;

    static {
        loadConfig();
    }
    
    private static void loadConfig(){
        /*if(SPRING_CONFIG_XML!=null ) return;
        List<SpringLoadConfig> configList = ImplServiceObject
            .getImplInterface(SPRING_IMPL_PATH, SpringLoadConfig.class);
        Collections.sort(configList, new Comparator<SpringLoadConfig>() {
            @Override
            public int compare(SpringLoadConfig o1, SpringLoadConfig o2) {
                return o1.getOrderNo() - o2.getOrderNo();
            }
        });
        String[] config = new String[0];
        for (SpringLoadConfig load : configList) {
            int configLength = config.length;
            config = Arrays.copyOf(config, configLength + load.getConfigName().length);// 扩容
            System.arraycopy(load.getConfigName(), 0, config, configLength, load.getConfigName().length);
        }*/
       // SPRING_CONFIG_XML = config;
    }
    
    @Override
    protected WebApplicationContext initWebApplicationContext() {
        super.setApplicationContext(getApplicationContext());
        return super.initWebApplicationContext();
    }

    @Override
    protected void initStrategies(ApplicationContext context) {
        super.initStrategies(context);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setInitParameter("contextConfigLocation", allToStr());
        contextLoaderListener.contextInitialized(sce);
        setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()));
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        contextLoaderListener.contextDestroyed(sce);
    }

    /**
     * 保存ApplicationContext。
     * 
     * @throws RuntimeException
     *             当企图第二次设置ApplicationContext时抛出该异常。
     */
    public void setApplicationContext(ApplicationContext ctx) {
        synchronized ("Spring.applicationContext") {
            if (applicationContext == null) {
                applicationContext = ctx;
            }
        }
    }

    public static ApplicationContext getApplicationContext() {
        synchronized ("Spring.applicationContext") {
            if (applicationContext == null) {
                applicationContext = new ClassPathXmlApplicationContext(SPRING_CONFIG_XML);
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
    public static <T> T getBean(String beanId, Class<T> requiredType) {
        return getApplicationContext().getBean(beanId, requiredType);
    }

    /**
     * 系统里所有BEAN
     * 
     * @return
     */
    public static String[] getAllBeanName() {
        return getApplicationContext().getBeanDefinitionNames();
    }

    private String allToStr() {
        StringBuilder sb = new StringBuilder();
        for (String conf : SPRING_CONFIG_XML) {
            sb.append("classpath:").append(conf).append(",");
        }
        return sb.toString();
    }

}
