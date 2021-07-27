/*
 * @(#)SwiftDubboServer.java   1.0  2021年7月13日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.dubbo;

import java.util.LinkedList;
import java.util.List;

import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.registry.support.AbstractRegistryFactory;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.swift.core.api.OpenApi;
import com.swift.core.server.LifeCycle;
import com.swift.core.spring.proxy.AnnotationTypeUtil;
import com.swift.server.dubbo.core.DubboMessageSender;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.BeanUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月13日
 */
@Component
public class SwiftDubboServer implements LifeCycle{

    @Value("${sysId:}")
    private String sysId;
    
    @Autowired(required=false)
    private ProviderConfig providerConfig;
    
    @Autowired(required=false)
    private DubboMessageSender dubboMessageSender;
    
    private  List<ServiceConfig<GenericService>> serviceList=new LinkedList<>();
    
    /** 
     * @see com.swift.core.server.LifeCycle#start(int)
     */
    @Override
    public void start(int port) {
        if(providerConfig==null) return;
        List<String> list = AnnotationTypeUtil.getAnnotationClassList(OpenApi.class);
        if(TypeUtil.isNull(list)) return;
        for(String beanClassName:list){
            Class<?> cla = BeanUtil.classForName(beanClassName);
            OpenApi openApi = AnnotationUtil.getAnnotation(cla, OpenApi.class);
            if(openApi==null) continue;
            if(!sysId.equalsIgnoreCase(openApi.value())) continue;
            ServiceConfig<GenericService> service = new ServiceConfig<>();
            service.setProvider(providerConfig);
            service.setInterface(beanClassName);
            service.setRef(dubboMessageSender);
            service.export();
            serviceList.add(service);
        }
    }

    /** 
     * @see com.swift.core.server.LifeCycle#isStarted()
     */
    @Override
    public boolean isStarted() {
        return !serviceList.isEmpty();
    }

    /** 
     * @see com.swift.core.server.LifeCycle#stop()
     */
    @Override
    public void stop() {
        AbstractRegistryFactory.destroyAll();
        return;
    }

}
