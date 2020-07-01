/*
 * @(#)RsaTest.java   1.0  2011-9-20
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.util.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * 加密、解密、信息摘要工具类。
 * <p>
 * 
 * 支持MD5、RSA等算法。
 * 
 * @author jiajin
 * @version 1.0 2011-9-20
 */
public class RsaUtils {

    /**
     * 返回SHA1信息摘要。
     */
    public static byte[] sha1(byte[] plainBytes) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(plainBytes);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * 生成RSA密钥，包括公钥和私钥。
     * 
     * @param length
     *            密钥长度
     */
    public static KeyPair genRsaKey(int length) throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(length, new SecureRandom());
        KeyPair pair = gen.generateKeyPair();
        return pair;
    }

    /**
     * 执行RAS加密（ECB/PKCS1Padding模式）。
     * 
     * @param plainBytes
     *            待加密数据
     * @param key
     *            密钥（可以是公钥也可以是私钥）
     */
    public static byte[] encryptRsa(byte[] plainBytes, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 在用RSA算法加密超过117个字节时，需要分段加密
        /*byte[] finalBytes = new byte[] {};
        int length = 50;
        for (int i = 0; i < plainBytes.length; i += length) {
            byte[] subArray = ArrayUtils.subarray(plainBytes, i, i + length);
            byte[] cipherBytes = cipher.doFinal(subArray);
            finalBytes = ArrayUtils.addAll(finalBytes, cipherBytes);
        }*/
        return cipher.doFinal(plainBytes);
       // return finalBytes;
    }

    /**
     * 执行RAS解密（ECB/PKCS1Padding模式）。
     * 
     * @param cryptBytes
     *            待解密数据
     * @param key
     *            密钥（可以是公钥也可以是私钥）
     */
    public static byte[] decryptRsa(byte[] cryptBytes, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 在用RSA算法解密超过128个字节时，需要分段解密
        /*byte[] finalBytes = new byte[] {};
        int length = 64;
        for (int i = 0; i < cryptBytes.length; i += length) {
            byte[] subArray = ArrayUtils.subarray(cryptBytes, i, i + length);
            byte[] plainBytes = cipher.doFinal(subArray);
            finalBytes = ArrayUtils.addAll(finalBytes, plainBytes);
        }

        return finalBytes;*/
        return cipher.doFinal(cryptBytes);
    }
    /**
     * 输入输出都是base64
     * @param value
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptRsa(String value, String key) {
        if(value==null) return null;
        if(value.length()<50) return value;
        try {
            byte[] v = decryptRsa(Base64.decodeBase64(value), loadPrivateKeyByStr(key));
            return new String(v, "UTF-8");
        } catch (Exception ex) {
        }
        return null;
    }
    
    
    
    /** 
     * 从字符串中加载公钥 
     *  
     * @param publicKeyStr 
     *            公钥数据字符串 
     * @throws Exception 
     *             加载公钥时产生的异常 
     */  
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)  
            throws Exception {  
        try {  
            byte[] buffer = Base64.decodeBase64(publicKeyStr);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法");  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("公钥非法");  
        } catch (NullPointerException e) {  
            throw new Exception("公钥数据为空");  
        }  
    }  
  
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)  
            throws Exception {  
        try {  
            byte[] buffer = Base64.decodeBase64(privateKeyStr);  
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法");  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("私钥非法");  
        } catch (NullPointerException e) {  
            throw new Exception("私钥数据为空");  
        }  
    }  
}
