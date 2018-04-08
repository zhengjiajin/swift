/*
 * @(#)ParserHtml.java   1.0  2015年8月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2015年8月14日
 */
@SuppressWarnings("unchecked")
public class ParserHtml {
    private final static Logger log = LoggerFactory.getLogger(ParserHtml.class);

    /**
     * 指定标签名
     * 
     * @param data
     *            Html源码
     * @param Tag
     *            指定要查找的Html标签
     * @param attribute
     *            指定标签中的属性
     * @throws DocumentException
     */
    public static List<String> parserHtml(String data, String tag, String attribute) throws DocumentException {
        List<String> list = new ArrayList<String>();
        Document doc = DocumentHelper.parseText(data);
        Element rootElement = doc.getRootElement();
        List<Element> elements = rootElement.selectNodes("//" + tag.toLowerCase());
        elements.addAll(rootElement.selectNodes("//" + tag.toUpperCase()));
        if (elements == null || elements.size() == 0) {
            return Collections.emptyList();
        }
        for (Element ele : elements) {
            try {
                Attribute att = ele.attribute(attribute);
                list.add(att.getValue());
            } catch (Exception e) {
                log.error("html解释错误:", e);
                continue;
            }
        }
        return list;
    }

    private static String clearHtmlTab(String strHtml) {
        String text = "<[^>]+>";
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(strHtml);
        return matcher.replaceAll("");
    }

    private static String cleanHtmlNull(String strHtml) {
        String text = "\\s*|\t|\r|\n";
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(strHtml);
        return matcher.replaceAll("");
    }

    private static String cleanHtmlOther(String strHtml) {
        String text = "&[^(;|&)]+;";
        Pattern pattern = Pattern.compile(text);
        Matcher matcher = pattern.matcher(strHtml);
        return matcher.replaceAll("");
    }

    /**
     * 清除html中的html元素标签
     * 
     * @param strHtml
     * @return
     */
    public static String cleanHtml(String strHtml) {
        if (StringUtils.isBlank(strHtml)) return "";
        return cleanHtmlNull(cleanHtmlOther(clearHtmlTab(strHtml)));
    }

    /**
     * 获取图片
     * 
     * @param html
     * @return
     */
    public static List<String> getImgUrlFromHTML(String html) {
        List<String> list = new ArrayList<String>();
        if (html == null) return list;
        Pattern PATTERN = Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)", Pattern.CASE_INSENSITIVE
            | Pattern.MULTILINE);
        Matcher matcher = PATTERN.matcher(html);
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group == null) {
                continue;
            }
            // 这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符
            if (group.startsWith("'")) {
                list.add(group.substring(1, group.indexOf("'", 1)));
            } else if (group.startsWith("\"")) {
                list.add(group.substring(1, group.indexOf("\"", 1)));
            } else {
                list.add(group.split("\\s")[0]);
            }
        }
        return list;
    }

    /**
     * 获取knowList图片
     * 
     * @param html
     * @return
     */
    public static List<String> getKnowListImgUrlFromHTML(String html) {
        List<String> list = new ArrayList<String>();
        String endStr = "!knowList";
        if (html == null) return list;
        Pattern PATTERN = Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)", Pattern.CASE_INSENSITIVE
            | Pattern.MULTILINE);
        Matcher matcher = PATTERN.matcher(html);
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group == null) {
                continue;
            }
            // 这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符
            if (group.startsWith("'")) {
                list.add(group.substring(1, group.indexOf("'", 1)) + endStr);
            } else if (group.startsWith("\"")) {
                list.add(group.substring(1, group.indexOf("\"", 1)) + endStr);
            } else {
                list.add(group.split("\\s")[0] + endStr);
            }
        }
        return list;
    }

    
}
