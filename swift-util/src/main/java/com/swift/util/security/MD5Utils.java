package com.swift.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.util.io.ByteUtil;

public class MD5Utils {
    private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);
	
    /**
     * MD5
     * @param value
     * @return
     */
    public static byte[] md5(byte[] value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(value);
        } catch (NoSuchAlgorithmException ex) {
            log.error("MD5签名失败",ex);
        }
        return null;
    }
    
    /**
     * MD5后输出16进制小写
     * @param strObj
     * @return
     */
    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = ByteUtil.bytesToHex(md.digest(strObj.getBytes())).toLowerCase();
        } catch (NoSuchAlgorithmException ex) {
            log.error("MD5签名失败",ex);
        }
        return resultString;
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.getMD5Code("1207970869BZTX12345678s"));
    }
}
