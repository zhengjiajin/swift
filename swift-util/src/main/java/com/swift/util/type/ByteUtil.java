/*
 * @(#)ByteUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.type;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.exception.extend.SystemException;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class ByteUtil {

    private final static Logger log = LoggerFactory.getLogger(ByteUtil.class);

    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 把十六进制串（HEX）转换为byte[]。
     * 
     * @param hex
     *            大小写均可
     */
    public static byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException(hex);
        }
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] chars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            byte hi = (byte) "0123456789ABCDEF".indexOf(Character.toUpperCase(chars[i * 2]));
            byte lo = (byte) "0123456789ABCDEF".indexOf(Character.toUpperCase(chars[i * 2 + 1]));
            if (hi < 0 || lo < 0) {
                throw new IllegalArgumentException(hex);
            }
            result[i] = (byte) (hi << 4 | lo);
        }
        return result;
    }

    /**
     * 把byte[]转换为十六进制串（HEX）。
     * 
     * @return 大写
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            char hi = Character.forDigit((bytes[i] >> 4) & 0x0F, 16);
            char lo = Character.forDigit(bytes[i] & 0x0F, 16);
            sb.append(Character.toUpperCase(hi));
            sb.append(Character.toUpperCase(lo));
        }
        return sb.toString();
    }

    /**
     * 解压
     * 
     * @param b
     * @return @
     */
    public static byte[] uncompress(byte[] b) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            out.close();
            return out.toByteArray();
        } catch (IOException ex) {
            log.error("IO异常", ex);
            throw new SystemException("IO异常");
        }
    }

    /**
     * 压缩
     * 
     * @param b
     * @return @
     */
    public static byte[] compress(byte[] b) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gout = new GZIPOutputStream(out);
            gout.write(b);
            gout.close();
            return out.toByteArray();
        } catch (IOException ex) {
            log.error("IO异常", ex);
            throw new SystemException("IO异常");
        }
    }

    public static byte[] getInputStream(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b_in = new byte[1024];
            int length = 0;
            while ((length = in.read(b_in)) > 0) {
                out.write(b_in, 0, length);
            }
            out.close();
            return out.toByteArray();
        } catch (IOException ex) {
            log.error("IO异常", ex);
            throw new SystemException("IO异常");
        }
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    /**
     * 抛出IOException会使Jetty返回HTTP 500错误，并关闭连接。
     */
    public static String readBody(BufferedReader reader) {
        try {
            StringBuilder sb = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } catch (IOException ex) {
            log.error("IO异常", ex);
            throw new SystemException("IO异常");
        }
    }
    // byte转char

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    public static final InputStream byteToInput(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static final byte[] inputToByte(InputStream inStream) {
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in2b = swapStream.toByteArray();
            return in2b;
        } catch (IOException ex) {
            log.error("IO异常", ex);
            throw new SystemException("IO异常");
        }
    }

    public static byte[] toByteArray(String filename) {

        File f = new File(filename);
        if (!f.exists()) {
            throw new SystemException(filename + "文件不存在");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException ex) {
            log.error("IO异常", ex);
            throw new SystemException("IO异常");
        } finally {
            try {
                in.close();
                bos.close();
            } catch (IOException e) {
                log.error("IO异常", e);
            }
        }
    }

    public static ByteBuffer stringToByteBuffer(String str) {
        return ByteBuffer.wrap(str.getBytes());
    }

    /**
     * ByteBuffer 转换 String
     * 
     * @param buffer
     * @return
     */
    public static String byteBufferToString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            throw new SystemException("IO异常",ex);
        }
    }

}
