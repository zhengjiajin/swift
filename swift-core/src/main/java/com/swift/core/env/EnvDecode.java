/*
 * @(#)EnvDecode.java   1.0  2018年7月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.env;

import com.swift.util.security.AESUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年7月2日
 */
public class EnvDecode {
    //使用加密需要用到的环境变量名称
    private final static String ENV_PASSWORD = "SWIFT_PASSWORD";
    
    public static String decode(String value) {
        String encryptKey = System.getenv(ENV_PASSWORD);
        if (TypeUtil.isNull(encryptKey)) return value;
        return AESUtil.decrypt(value, encryptKey);
    }

    public static void main(String[] args) {
        System.out.println(System.getenv("SWIFT_PASSWORD"));
        System.out.println(System.getenv().keySet());
        System.out.println(AESUtil.encrypt("hhmkroot", "sYHN"));
    }

}