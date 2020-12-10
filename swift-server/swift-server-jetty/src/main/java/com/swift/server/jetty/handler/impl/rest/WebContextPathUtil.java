/*
 * @(#)WebContextPathUtil.java   1.0  2018年6月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.jetty.handler.impl.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月8日
 */
public class WebContextPathUtil {
    private final static Logger log = LoggerFactory.getLogger(WebContextPathUtil.class);

    private static List<String> context_path = new ArrayList<String>();

    /**
     * URL是否通过此服务 contextPath=NULL，则都通过
     * 
     * @param target
     * @param contextPath
     * @return
     */
    public static boolean isContextPath(String target, String contextPath) {
        log.debug("target:" + target + ";" + contextPath);
        if (contextPath == null) return true;
        if (target == null) return false;
        if (!target.startsWith(contextPath)) {
            return false;
        }
        // 找出最长的ContextPath
        String pathTarget = "";
        for (String path : context_path) {
            if (target.startsWith(path) && pathTarget.length() < path.length()) {
                pathTarget = path;
            }
        }
        return pathTarget.equals(contextPath);
    }

    public static void setContextPath(String contextPath) {
        if (!context_path.contains(contextPath) && contextPath != null) {
            context_path.add(contextPath);
        }
    }
}
