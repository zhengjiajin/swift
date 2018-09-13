/*
 * @(#)EmojiCleanUtil.java   1.0  2015年8月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.layout;


/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2015年8月29日
 */
public class EmojiCleanUtil {
    /**
     * Emoji String
     * @param source
     * @return
     */
    public static boolean isEmojiString(String source) {
        if (source == null || source == "") {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            String codePoint = source.substring(i, i+1);
            //三位的直接跳过
            if(codePoint.getBytes().length==3) continue;
            char charStr = source.charAt(i);
            if (isEmojiChar(charStr)) {
                return true;
            }
        }
        return false;
    }
    /**
     * emoji转为二位char
     * @param codePoint
     * @return
     */
    private static boolean isEmojiChar(char codePoint) {
        return ((codePoint >= 0x1000) && (codePoint <= 0xFFFF)) ;
    }
    

    /**
     * 取出表情符
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (!isEmojiString(source)) {
            return null;
        }
        StringBuilder buf = new StringBuilder(source.length());
        int len = source.length();
        for (int i = 0; i < len; i++) {
            String codeStr = source.substring(i, i+1);
            //三位的直接跳过
            if(codeStr.getBytes().length==3) continue;
            
            char codePoint = source.charAt(i);
            if (isEmojiChar(codePoint)) {
                buf.append(codePoint);
            }
        }
        return buf.toString();
    }
    
    /**
     * 取出表情符
     * @param source
     * @return
     */
    public static String cleanEmoji(String source) {
        if (!isEmojiString(source)) {
            return source;
        }
        StringBuilder buf = new StringBuilder(source.length());
        int len = source.length();
        for (int i = 0; i < len; i++) {
            String codeStr = source.substring(i, i+1);
            //三位的直接跳过
            if(codeStr.getBytes().length==3) {
                buf.append(codeStr);
            }else{
                char codePoint = source.charAt(i);
                if (!isEmojiChar(codePoint)) {
                    buf.append(codePoint);
                }
            }
        }
        return buf.toString();
    }

}
