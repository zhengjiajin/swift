/*
 * @(#)DefaultAuthKeyInterface.java   1.0  2020年4月16日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.auth.impl;

import com.swift.core.auth.AuthKeyInterface;
import com.swift.core.spring.Spring;
import com.swift.util.type.TypeUtil;

/**
 * ${}格式优先级->.properties-Dargname->环境变量 
 * @author zhengjiajin
 * @version 1.0 2020年4月16日
 */
public class DefaultAuthKeyInterface implements AuthKeyInterface {

    
    /** 
     * @see com.swift.core.auth.AuthKeyInterface#getKey(java.lang.Object)
     */
    @Override
    public byte[] getKey(String toSysId,Object keyRem) {
        if(TypeUtil.isNull(keyRem)) return null;
        String keyFild = String.valueOf(keyRem);
        if(!keyFild.startsWith("${")) return keyFild.getBytes();
        if(!keyFild.endsWith("}")) return keyFild.getBytes();
        keyFild=keyFild.substring(2,keyFild.length()-1);
        String key = null;
        key = Spring.getProperties().getProperty(keyFild);
        if(TypeUtil.isNotNull(key)) return key.getBytes();
        key = System.getProperty(keyFild);
        if(TypeUtil.isNotNull(key)) return key.getBytes();
        key = System.getenv(keyFild);
        if(TypeUtil.isNotNull(key)) return key.getBytes();
        return null;
    }

}
