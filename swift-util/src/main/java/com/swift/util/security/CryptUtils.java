/*
 * @(#)RsaTest.java   1.0  2011-9-20
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.util.security;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

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
public class CryptUtils {

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
     * 生成DES加密算法的密钥
     * 
     * @return 密钥的二进制流
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static Key genDesKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }

    /**
     * 执行DES算法加密（CBC/PKCS5Padding模式）。
     * 
     * @param plainBytes
     *            待加密的明文
     * @param key
     *            密钥
     * @return 加密后的密文
     * @throws GeneralSecurityException
     */
    public static byte[] encryptDes(byte[] plainBytes, Key key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(plainBytes);
    }

    /**
     * 执行DES算法解密（CBC/PKCS5Padding模式）。
     * 
     * @param cipherBytes
     *            待解密的密文
     * @param key
     *            密钥
     * @param iv
     *            初始化向量
     * @return 解密后的明文
     * @throws GeneralSecurityException
     */
    public static byte[] decryptDes(byte[] cipherBytes, Key key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherBytes);
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
    
    public static void main(String[] args) {
//        String a="T3MIlOfoZkQkU7SXuC6vYKgGVHDgyXQVFpxV4cWw7odZGomOIXkMMK5KZsTT8+rMH+wdtxTdUo+m42QW/FPTuBjob5j7JU1iQxBynm0/Zm9P0hw9oC2TmU2fPMRoHecKQRCRMkmgruxqcZnUeFNXnGclHdUAvMJJL36cTKBAqr0=";
//        String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMhqDEkM1Dd9ZvWHwwxCC+2orObRYZbwkdhGfRPwY6bs5d+vFMT9pNpZHD36tSx+t8JGyvMfvAT8D2M//6SiKN1LZr865Bpv9oVBqbE6FBOLu7+iR4Kdaqtl1z0Wk2AEq/igO75ZWh+0+VSsNWgCGHRF0Xf/g4atIx1bwHENoYRzAgMBAAECgYBKqKbW975+xWp7gQ26HUiQSkvO2KVAM3JBXVDvApg88PdZcsGL+OlWpeeNdz3Sy4ntB65HD8VdHgElegvj6rzJurAmhyS8ms3/PUGN349GKc11QfMjbN8bC+a43vCfEXhi9Cj9vyOFsxmxoRoeWmqb671q6zVomniv+GoRvNpaUQJBAOKKJZhpWTyuJaWmzBegQZW3cjF+bHmNp5LIJ5K0gaR96xHwbWMrYJN/M5Otto7Ul1ed3czSC09HYe+bWqTPvYUCQQDieiWV3MHrDOBstkuG76kDYTIJdFNwnR2Lx2qENiGcDzhlIKk6yc03lmQs8CMnC1dRIaTNlf15vKsobV/9MT+XAkEAmym0BmhhzsYY0xUjxSrmwP0dIflNoxP7KvYc5Mbt5RS55VegFMSpKV1+ni/lUn8aCdD/EkLrrClgUxU9SJVhYQJACmbVxxZGiccjn5qXyXDEcHVMWQbQtkFhFARqrfvWe1ZvnqVrYFnYZQ8xNqco1ig9MGq9KPwUkjzKF6c+W2ChrQJATKxdqCedVC3X8lQwqpaKLoesu692shsGfgjMBmtsxnNb5aI4rMrv44h0rOEfs2pSBfgnAu0XfBaSui+kVhFW1A==";

    	String a = "C8//FsknQ2IX18nEYK7kLv7iFJ9kd8c0UPG++QFIRf+yfexf9GixUzQ4dKbDdJADIs0cG7xbg5cw+9/XIL4fYH7Za+p/x3Gt79VrUECzae/U3Yj059Gn3fC1UblvB6aOZ0V8+QRSUsqkgnX++cnGb5kAiSDrZ8wEbm6KXcd8jSY=";
    	String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMhqDEkM1Dd9ZvWHwwxCC+2orObRYZbwkdhGfRPwY6bs5d+vFMT9pNpZHD36tSx+t8JGyvMfvAT8D2M//6SiKN1LZr865Bpv9oVBqbE6FBOLu7+iR4Kdaqtl1z0Wk2AEq/igO75ZWh+0+VSsNWgCGHRF0Xf/g4atIx1bwHENoYRzAgMBAAECgYBKqKbW975+xWp7gQ26HUiQSkvO2KVAM3JBXVDvApg88PdZcsGL+OlWpeeNdz3Sy4ntB65HD8VdHgElegvj6rzJurAmhyS8ms3/PUGN349GKc11QfMjbN8bC+a43vCfEXhi9Cj9vyOFsxmxoRoeWmqb671q6zVomniv+GoRvNpaUQJBAOKKJZhpWTyuJaWmzBegQZW3cjF+bHmNp5LIJ5K0gaR96xHwbWMrYJN/M5Otto7Ul1ed3czSC09HYe+bWqTPvYUCQQDieiWV3MHrDOBstkuG76kDYTIJdFNwnR2Lx2qENiGcDzhlIKk6yc03lmQs8CMnC1dRIaTNlf15vKsobV/9MT+XAkEAmym0BmhhzsYY0xUjxSrmwP0dIflNoxP7KvYc5Mbt5RS55VegFMSpKV1+ni/lUn8aCdD/EkLrrClgUxU9SJVhYQJACmbVxxZGiccjn5qXyXDEcHVMWQbQtkFhFARqrfvWe1ZvnqVrYFnYZQ8xNqco1ig9MGq9KPwUkjzKF6c+W2ChrQJATKxdqCedVC3X8lQwqpaKLoesu692shsGfgjMBmtsxnNb5aI4rMrv44h0rOEfs2pSBfgnAu0XfBaSui+kVhFW1A==";
    	//byte[] kk = Base64.getDecoder().decode(key);
//        byte[] vv = Base64.getDecoder().decode(a);
        try {
//            RSAPrivateKey rsakey = loadPrivateKeyByStr(key);
//            byte[] aaa = decryptRsa(vv, rsakey);
//            System.out.println(new String(aaa,"utf-8"));
        	
        	String plain = decryptRsa(a, key);
        	System.out.println(plain);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main2(String[] args) throws Exception {
        KeyPair keyPair = genRsaKey(1024);
        PrivateKey prik = keyPair.getPrivate();
        PublicKey pubk = keyPair.getPublic();
        String prik_str = Base64.encodeBase64String(prik.getEncoded());
        String pubk_str = Base64.encodeBase64String(pubk.getEncoded());
        System.out.println("prik_str:"+prik_str);
        System.out.println("pubk_str:"+pubk_str);
        
        String vv = "uioda";
        
        byte[] pubk_v =  encryptRsa(vv.getBytes(), pubk);
        System.out.println("加密成功");
        System.out.println(new String(decryptRsa(pubk_v, prik)));
        
        RSAPrivateKey rsakey = loadPrivateKeyByStr(prik_str);
        
        System.out.println(new String(decryptRsa(pubk_v, rsakey)));
    }

    public static void main3(String[] args) throws Exception {
        byte[] h = encryptDes("sdfasdzzzzzzzzzzz测试".getBytes(), genDesKey("12345678"),
                new IvParameterSpec("11111111".getBytes()));
        System.out.println(new String(h));
        System.out.println(h.length);
        System.out.println(new String(decryptDes(h, genDesKey("12345678"), new IvParameterSpec("11111111".getBytes()))));
    }

    public static void main4(String[] args) throws Exception {
        // 测试MD5
        /*long t1 = System.currentTimeMillis();
        // 如果要测试性能，把1改为1000000。
        for (int i = 0; i < 1; i++) {
            byte[] bytes = md5("admin".getBytes());
            System.out.println("MD5:" + BytesUtils.bytesToHex(bytes));
        }
        System.out.println("time: " + (System.currentTimeMillis() - t1));
*/
        // 测试RSA：生成密钥、加密、解密
        KeyPair keyPair = genRsaKey(512);
        System.out.println(keyPair.getPrivate().getEncoded());
        keyPair.getPrivate();
        //PrivateKey key = RSAPrivateCrtKeySpec.
            /*byte[] plainBytes2 = BytesUtils.hexToBytes("00112233445566778899AABBCCDDEEFF");
        System.out.println("原文：" + BytesUtils.bytesToHex(plainBytes2));
        long t2 = System.currentTimeMillis();
        // 如果要测试性能，把1改为10000。
        for (int i = 0; i < 1; i++) {
            byte[] cryptBytes2 = encryptRsa(plainBytes2, keyPair.getPublic());
            System.out.println("加密：" + BytesUtils.bytesToHex(cryptBytes2));
            byte[] decryptBytes2 = decryptRsa(cryptBytes2, keyPair.getPrivate());
            System.out.println("解密：" + BytesUtils.bytesToHex(decryptBytes2));
        }
        System.out.println("time: " + (System.currentTimeMillis() - t2));*/
    }
}
