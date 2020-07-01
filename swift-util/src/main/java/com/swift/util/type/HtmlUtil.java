package com.swift.util.type;

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

public class HtmlUtil {

	private final static Logger log = LoggerFactory.getLogger(HtmlUtil.class);

	public static String delHTMLTag(String htmlStr) {
		if (TypeUtil.isNull(htmlStr)) {
			return null;
		}
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return cleanHtml(htmlStr.trim()); // 返回文本字符串
	}

	static Pattern pattern = Pattern.compile(
			"(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u0020\u3002\u201d\u201c\u30a1-\u30f6\u3041-\u3093\uFF00-\uFFFF\u4e00-\u9fa5]*");

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
		List<String> list = getUrl(html);
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
	@SuppressWarnings("unchecked")
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
		if (StringUtils.isBlank(strHtml))
			return "";
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
		if (html == null)
			return list;
		Pattern PATTERN = Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
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
}
