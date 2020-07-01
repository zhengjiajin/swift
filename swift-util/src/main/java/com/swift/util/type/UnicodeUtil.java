/*
 * @(#)UnicodeUtil.java   1.0  2019年4月18日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.type;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年4月18日
 */
public class UnicodeUtil {
    
    
    
    //单字符串转换unicode
    public static String stringToUnicode(String string) {
        if(TypeUtil.isNull(string)) return string;
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);  // 取出每一个字符
            unicode.append("\\u" +Integer.toHexString(c));// 转换为unicode
        }
        return unicode.toString();
    }

    //单unicode 转字符串
    public static String unicodeToString(String unicode) {
        if(TypeUtil.isNull(unicode)) return unicode;
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 0; i < hex.length; i++) {
            String change = "";
            String noChange="";
            if(hex[i].length()>4) {
                change=hex[i].substring(0, 4);
                noChange=hex[i].substring(4);
            } else {
                change=hex[i];
            }
            string.append(changeStr(change, noChange));// 追加成string
        }
        return string.toString();
    }
    
    private static String changeStr(String change,String noChange) {
        try {
            if(change.length()<=0) return noChange;
            int data = Integer.parseInt(change, 16);// 转换出每一个代码点
            return (char)data+noChange;
        }catch(Exception ex) {
            String newchange=change.substring(0,change.length()-1);
            String newnoChange=change.substring(change.length()-1)+noChange;
            if(newchange.length()<=0) return newnoChange;
            return changeStr(newchange, newnoChange);
        }
    }
    
    public static void main(String[] args) {
        String s = "ryan开启了朋友验证，你还不是他（她）朋友。请先发送朋友验证请求，对方验证通过后，才能聊天。\\u003ca href\\u003d\\\"weixin://findfriend/verifycontact\\\"\\u003e发送朋友验证\\u003c/a\\u003e";
        String u = stringToUnicode(unicodeToString(s));
        System.out.println(unicodeToString(u));
    }
    
}
