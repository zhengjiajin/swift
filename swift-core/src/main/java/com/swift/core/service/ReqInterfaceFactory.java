/*
 * @(#)ReqInterfaceFactory.java   1.0  2014-5-16
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.core.model.ServiceRequest;
import com.swift.core.spring.Spring;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.util.bean.AopTargetUtils;
import com.swift.util.type.TypeUtil;

/**
 * 服务工厂
 * @author jiajin
 * @version 1.0 2014-5-16
 */
public class ReqInterfaceFactory {
    private final static Logger log = LoggerFactory.getLogger(ReqInterfaceFactory.class);
    
    public final static String SEPARATE = "V"; //版本分割符
    
    public final static String DEFAULT_VERSION = ServiceRequest.DEFAULT_VERSION;
    
    private static Map<String,Set<String>> interfaceVersions = null;
    
    static{
        interfaceVersions = new HashMap <String,Set<String>>();
        String[] sprinAllbeanName = Spring.getAllBeanName(); // 扫描Spring所有bean的名称
        for(String beanName :sprinAllbeanName) {
            int idx = beanName.lastIndexOf(SEPARATE);
            String version = DEFAULT_VERSION; // 版本号
            String method = null; //能力
            if(idx > -1) {
                float temp = 0;
                try {
                    version = beanName.substring(idx + 1, beanName.length());
                    temp = Float.parseFloat(version);
                } catch (Exception ex) {
                    log.debug(ex.getMessage());
                }
                if( temp > 0 ) {
                    method = beanName.substring(0, idx);
                } else {
                    method = beanName;
                    version = DEFAULT_VERSION;
                }
            } else {
                method = beanName;
            }
            saveVersion(version, method);
        }
    }

    /**
     * 保存到能力与版本集合
     * @param version 版本号
     * @param method 能力
     */
    private static void saveVersion(String version, String method) {
        Set<String> versions = interfaceVersions.get(method);
        if(versions == null) {
            versions = new TreeSet<String> (new VersionComparator()); // 版本默认倒序
            versions.add(version);
            interfaceVersions.put(method, versions);
        } else {
            versions.add(version);
        }
    }

    /**
     * 
     * @param method 能力
     * @param version 版本号(必须是浮点型数字，不能出现字母)
     * @return
     */
    public static BaseInterface getInterface(String method, String version) {
        Set<String> versions = interfaceVersions.get(method);
        if (versions != null) {
            String serviceName = findVersion(method, version, versions);
            return Spring.getBean(serviceName, BaseInterface.class);
        } else {
            String msg = "服务能力" + method + "找不到";
            log.warn(msg);
            throw new ServiceException(ResultCode.NO_METHOD, msg);
        }
    }
    
    /**
     * 找到真实对象
     * @param method
     * @param version
     * @return
     */
    public static Object getInterfaceTrueObj(String method, String version) {
        try {
            return AopTargetUtils.getTarget(getInterface(method, version));
        } catch (Exception ex) {
            log.error(ResultCode.NO_METHOD+"",ex);
            throw new ServiceException(ResultCode.NO_METHOD, "服务能力" + method + "找不到");
        }
    }
    
	/**
	 * 找到真实对象，如果找不到返回null
	 * 
	 * @param method
	 * @param version
	 * @return
	 */
	public static Object getInterfaceTrueObjSlient(String method, String version) {
		Set<String> versions = interfaceVersions.get(method);
		if (versions != null) {
			String serviceName = findVersion(method, version, versions);
			SimpleInterface bean = Spring.getBean(serviceName, SimpleInterface.class);
			try {
				return AopTargetUtils.getTarget(bean);
			} catch (Exception ex) {
			}
		}
		return null;
	}

    /**
     * 寻找符合的版本号
     * @param method 能力
     * @param version App请求时的版本号
     * @param versions 服务器中能力与版本的集合
     * @return 完整的serviceName
     */
    private static String findVersion(String method, String version, Set<String> versions) {
        if(StringUtils.isNotBlank(version) && !DEFAULT_VERSION.equals(version)) {
            Iterator<String> it = versions.iterator();
            while (it.hasNext()) {
                String temp = it.next();
                if (TypeUtil.toFloat(version) >= TypeUtil.toFloat(temp)) {
                    version = temp; // 使用符合的版本,寻找最近发布的版本
                    break;
                }
            }
        } else if (versions !=null && !versions.isEmpty()){
            TreeSet<String> treeSet = (TreeSet<String>)versions;
            version = treeSet.last();
        }
        String serviceName = null;
        if(!DEFAULT_VERSION.equals(version)) {
            serviceName = method + SEPARATE + version; // version变成最符合的版本号
        } else {
            serviceName = method;
        }
        return serviceName;
    }
    
    /**
     * 版本顺序排序器
     * 添加说明 
     * @author kyj
     * @version 1.0 2015年8月18日
     */
    static class VersionComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            float f = TypeUtil.toFloat(o2) - TypeUtil.toFloat(o1);
            if (f > 0) {
                return 1;
            } else if (f < 0) {
                return -1;
            }
            return 0;
        }
    }
}
