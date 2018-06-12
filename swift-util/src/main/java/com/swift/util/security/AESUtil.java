package com.swift.util.security;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.LRUMap;

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
	 * use password to encrypt content by AES(128bit). 
	 * @param content	you want to encrypt message.
	 * @param password	the encrypt key you want to use.
	 * @return the content after encrypted.
	 */
	public static String encrypt(String content, String password){
		try {
			byte[] ptext = content.getBytes();
			byte[] ctext = AESUtil.getEncryptCipher(password).doFinal(ptext);
			return byte2hex(ctext);
		} catch(Exception e) {
			throw new CipherException(e);
		}
	}
	/**
	 * use password to decrypt content by AES(128bit).
	 * @param content	you want to decrypt content.
	 * @param password  the you want to use.
	 * @return	the content after decrypted.
	 */
	public static String decrypt(String content, String password) {
		try {
			byte[] ptext = AESUtil.getDecryptCipher(password).doFinal(hex2byte(content)); 
			return new String(ptext);
		} catch(Exception e) {
			throw new CipherException(e);
		}
	}
	
	private static final int maxSize = 100;
	private static LRUMap cacheEncryptCipher = new LRUMap(maxSize);
	private static LRUMap cacheDecryptCipher = new LRUMap(maxSize);
	private static Cipher getEncryptCipher(String password) {
		try {
			Cipher cp = (Cipher) cacheEncryptCipher.get(password);
			if(cp == null) {
				Key key = AESUtil.getKey(password);
				cp = Cipher.getInstance("AES");
				cp.init(Cipher.ENCRYPT_MODE, key);
				
				cacheEncryptCipher.put(password, cp);
			}
			return cp;
		} catch (Exception e) {
			throw new CipherException(e);
		}
	}
	private static Cipher getDecryptCipher(String password) {
		try {
			Cipher cp = (Cipher) cacheDecryptCipher.get(password);
			if(cp == null) {
				Key key = AESUtil.getKey(password);
				cp = Cipher.getInstance("AES");
				cp.init(Cipher.DECRYPT_MODE, key);
				
				cacheDecryptCipher.put(password, cp);
			}
			return cp;
		} catch (Exception e) {
			throw new CipherException(e);
		}
	}
	public static class CipherException extends RuntimeException {
		private static final long serialVersionUID = -7938919648349659765L;

		public CipherException(Exception e) {
	        super(e);
	    }
	}
	
	private static Key getKey(String publickey) {
		byte[] bytes = hex2byte(DigestUtils.md5Hex(publickey));
		return new SecretKeySpec(bytes, "AES");
	}
	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 != 0) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}

	public static String byte2hex(byte b[]) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < b.length; n++) {
			String stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1) {
				sb.append("0");
			}
			sb.append(stmp);
		}
		return sb.toString().toUpperCase();
	}

	public static void main(String[] args) {
		String key="zhangf";
		String content = "I'am testing...";
		String encode = AESUtil.encrypt(content, key);
		String base64 = Base64.encodeBase64String(AESUtil.hex2byte(encode));
		String decode = AESUtil.decrypt(encode, key);
		System.out.println(content+"\n"+encode+"\n"+base64+"\n"+decode);
	}
}
