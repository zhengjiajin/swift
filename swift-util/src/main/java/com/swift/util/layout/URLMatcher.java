/*
 * @(#)URLMatcher.java   1.0  2015年8月15日
 * 
 * Copyright (c)    2014-2020. All Rights Reserved. GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 添加说明
 * 
 * @author kyj
 * @version 1.0 2016年3月24日
 */
public class URLMatcher {

    static Pattern pattern = Pattern
        .compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u0020\u3002\u201d\u201c\u30a1-\u30f6\u3041-\u3093\uFF00-\uFFFF\u4e00-\u9fa5]*");

    // 匹配中文字符的正则表达式： [\u4e00-\u9fa5] 匹配双字节字符(包括汉字在内)：[^\x00-\xff]

    public static List<String> getUrl(String html) {
        // 中文结束
        Matcher matcher = pattern.matcher(html);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 正文内容加上a标签
     * 
     * @param html
     * @return
     */
    public static String replaceUrl(String html) {
        List<String> list = URLMatcher.getUrl(html);
        StringBuffer sbText = new StringBuffer();
        if (list.isEmpty()) {
            sbText.append(html);
        } else {
            int index = 0;
            for (String s : list) {
                int q = html.indexOf(s, index);
                String start = html.substring(index, q);
                String a = "<a href='" + s + "'>" + s + "</a>";
                sbText.append(start);
                sbText.append(a);
                index = q + s.length();
            }
            String end = html.substring(index);
            sbText.append(end);
        }
        return sbText.toString();
    }

    public static void main(String[] args) {
        List<String> list = URLMatcher
            .getUrl("随碟附送下载地址http://www.zuidaima.com/sdfsdf.htm?aaaa=%ee%sss http://www.zuidaima.com/sdfsdf.htm?aaaa=%ee%sss，，“。。结束");
        System.out.println(list);
        System.out
            .println(URLMatcher
                .replaceUrl("随碟附送下载地址http://www.zuidaima.com/sdfsdf.htm?aaaa=%ee%sss http://www.zuidaima.com/sdfsdf.htm?aaaa=%ee%sss，，“。。结束"));
    }
}
