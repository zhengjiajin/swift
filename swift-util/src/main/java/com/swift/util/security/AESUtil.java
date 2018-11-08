package com.swift.util.security;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.LRUMap;

import com.swift.exception.UnknownException;
import com.swift.util.io.ByteUtil;

/**
 * This class is used for ...
 * AES Coder<br/> 
 * secret key length:   128bit, default:    128 bit<br/> 
 * Generated through md5.
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/> 
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/ 
 *
 */
public class AESUtil {
	/**
	 * 16进制字段串加解密
	 * @param content	you want to encrypt message.
	 * @param password	the encrypt key you want to use.
	 * @return the content after encrypted.
	 */
	public static String encrypt(String content, String password){
		try {
		    byte[] pwd = ByteUtil.hexToBytes(DigestUtils.md5Hex(password));
			byte[] ctext = AESUtil.getEncryptCipher(pwd).doFinal(content.getBytes());
			return ByteUtil.bytesToHex(ctext);
		} catch(Exception e) {
		    throw new UnknownException("AES异常",e);
		}
	}
	/**
	 * 16进制字段串加解密
	 * @param content	you want to decrypt content.
	 * @param password  the you want to use.
	 * @return	the content after decrypted.
	 */
	public static String decrypt(String content, String password) {
		try {
		    byte[] pwd = ByteUtil.hexToBytes(DigestUtils.md5Hex(password));
			byte[] ptext = AESUtil.getDecryptCipher(pwd).doFinal(ByteUtil.hexToBytes(content)); 
			return new String(ptext);
		} catch(Exception e) {
		    throw new UnknownException("AES异常",e);
		}
	}
	
	/**
     * use password to encrypt content by AES(128bit). 
     * @param content   you want to encrypt message.
     * @param password  the encrypt key you want to use.
     * @return the content after encrypted.
     */
    public static byte[] encrypt(byte[] content, byte[] password){
        try {
            byte[] ctext = AESUtil.getEncryptCipher(password).doFinal(content);
            return ctext;
        } catch(Exception e) {
            throw new UnknownException("AES异常",e);
        }
    }
    /**
     * use password to decrypt content by AES(128bit).
     * @param content   you want to decrypt content.
     * @param password  the you want to use.
     * @return  the content after decrypted.
     */
    public static byte[] decrypt(byte[] content, byte[] password) {
        try {
            byte[] ptext = AESUtil.getDecryptCipher(password).doFinal(content); 
            return ptext;
        } catch(Exception e) {
            throw new UnknownException("AES异常",e);
        }
    }
	
	private static final int maxSize = 100;
	private static LRUMap cacheEncryptCipher = new LRUMap(maxSize);
	private static LRUMap cacheDecryptCipher = new LRUMap(maxSize);
	private static Cipher getEncryptCipher(byte[] password) {
		try {
			Cipher cp = (Cipher) cacheEncryptCipher.get(password);
			if(cp == null) {
				Key key = new SecretKeySpec(password, "AES");
				cp = Cipher.getInstance("AES");
				cp.init(Cipher.ENCRYPT_MODE, key);
				cacheEncryptCipher.put(password, cp);
			}
			return cp;
		} catch (Exception e) {
		    throw new UnknownException("AES异常",e);
		}
	}
	private static Cipher getDecryptCipher(byte[] password) {
		try {
			Cipher cp = (Cipher) cacheDecryptCipher.get(password);
			if(cp == null) {
			    Key key = new SecretKeySpec(password, "AES");
				cp = Cipher.getInstance("AES");
				cp.init(Cipher.DECRYPT_MODE, key);
				cacheDecryptCipher.put(password, cp);
			}
			return cp;
		} catch (Exception e) {
			throw new UnknownException("AES异常",e);
		}
	}
	
	public static void main(String[] args) throws Exception {
	    
	    String key="zhangf";
        String content = "I'am testing...";
        String encode = AESUtil.encrypt(content, key);
        String decode = AESUtil.decrypt(encode, key);
        System.out.println(content+"\n"+encode+"\n"+decode);
	}
	
}