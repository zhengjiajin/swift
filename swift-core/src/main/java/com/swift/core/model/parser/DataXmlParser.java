/*
 * @(#)DataXmlParser.java   1.0  2015年8月17日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.parser;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;

import com.ctc.wstx.stax.WstxInputFactory;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.exception.ServiceException;
import com.swift.exception.extend.SystemException;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2015年8月17日
 */
public class DataXmlParser {
    private static XMLInputFactory xmlInputFactory;
    static {
        // StAX规范并未要求XMLInputFactory/XMLOutputFactory线程安全。
        // SUN JDK6自带StAX实现非线程安全，而Wstx实现为线程安全，且性能优于JDK6自带实现（快10~30%）。
        xmlInputFactory = new WstxInputFactory();
        xmlInputFactory.setProperty("javax.xml.stream.isCoalescing", true);
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    }

    /**
     * 把XML标签解析成一个DataModel。
     * 
     * @param xml
     *            。
     * @return DataModel。
     */
    public static DataModel xmlToObject(String xml) {
        XMLEventReader r = null;
        try {
            r = xmlInputFactory.createXMLEventReader(new StringReader(xml));
        	r = xmlInputFactory.createFilteredReader(r, new EventFilter() {
				
				@Override
				public boolean accept(XMLEvent event) {
					// We are going to disregard characters that are new line and whitespace
	                if (event.isCharacters()) {
	                    Characters chars = event.asCharacters();
	                    String data = chars.getData();
	                    return valid(data);
	                } else {
	                    return event.isStartElement() || event.isEndElement();
	                }
				}
				
				private boolean valid(String str) {
	                return str != null && str.trim().length() > 0;
	            }
			});
            removeDataStartElement(r);
            DataModel data = new MapDataModel();
            parseXml(r, data);
            return data;
        } catch (XMLStreamException ex) {
            throw new SystemException("字段格式错误", ex);
        } finally {
            try {
                r.close();
            } catch (Exception ex) {
            }
        }
    }
    
	/**
	 * 把DataModel对象序列化为XML
	 * 
	 * <pre>
	 * <strong>注：
	 * <ul>
	 * <li>如果DataModel对象中的元素为复杂类型（DataModel, List和数组除外），则只是简单的调用对象的toString()方法
	 * </ul>
	 * </strong>
	 * </pre>
	 * 
	 * @param data
	 * @return
	 */
	public static String objectToXml(DataModel data) {
		return objectToXml(data, null);
	}

	/**
	 * 把DataModel对象序列化为XML，指定根元素的名称
	 * 
	 * <pre>
	 * <strong>注：
	 * <ul>
	 * <li>如果DataModel对象中的元素为复杂类型（DataModel, List和数组除外），则只是简单的调用对象的toString()方法
	 * </ul>
	 * </strong>
	 * </pre>
	 * 
	 * @param data
	 * @return
	 */
	public static String objectToXml(DataModel data, String root) {
		StringBuilder buf = new StringBuilder();
		serializeToXml(buf, data, root);
		return buf.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private static void serializeToXml(StringBuilder buf, DataModel data, String parent) {
		if (data == null) {
			return;
		}
		if (parent == null || parent.trim().isEmpty()) {
			parent = "xml";
		}
		buf.append("<").append(parent).append(">");
		for (String key : data.keySet()) {
			Object value = data.getObject(key);
			if (value == null) {
				buf.append("<").append(key).append(">");
				buf.append("</").append(key).append(">");
			} else if (value instanceof DataModel) {
				serializeToXml(buf, (DataModel) value, key);
			} else if (value instanceof List) {
				serializeListToXML(buf, (List) value, key);
			} else if (value.getClass().isArray()) {
				serializeListToXML(buf, Arrays.asList((Object[]) value), key);
			} else {
				buf.append("<").append(key).append(">");
				buf.append("<![CDATA[").append(value.toString()).append("]]>");
				buf.append("</").append(key).append(">");
			}
		}
		buf.append("</").append(parent).append(">");
	}
	
	@SuppressWarnings("rawtypes")
	private static void serializeListToXML(StringBuilder buf, List list, String parent) {
		for (Object item : list) {
			if (item == null) {
				buf.append("<").append(parent).append(">");
				buf.append("</").append(parent).append(">");
			} else if (item instanceof DataModel) {
				serializeToXml(buf, (DataModel) item, parent);
			} else if (item instanceof List) {
				serializeListToXML(buf, (List) item, parent);
			} else {
				buf.append("<").append(parent).append(">");
				buf.append("<![CDATA[").append(item.toString()).append("]]>");
				buf.append("</").append(parent).append(">");
			}
		}
	}

    private static void parseXml(XMLEventReader r, DataModel parent)
        throws XMLStreamException {
        Object object=null;
        while (r.hasNext()) {
            XMLEvent e = r.nextEvent();
            if (e.isStartElement()) {
                if (r.peek().isStartElement()) {
                    object = new MapDataModel();
                    parseXml(r,  (DataModel) object);
                }
            }else if(e.isCharacters()){
                String data = e.asCharacters().getData();
                if(!isBlank(data)){
                    object=data;
                }
            }else if(e.isEndElement()){
                String currentTag = e.asEndElement().getName().getLocalPart();
                parent.addObject(currentTag,object);
                if(r.peek().isEndElement()){
                    return;
                }
            }
            
        }
    }
    
    private static boolean isBlank(String data){
        if(StringUtils.isBlank(data)) return true;
        String xmlStr = data.toString().replace("\r", "").replace("\n", "").replace("\t", "");
        if(StringUtils.isBlank(xmlStr)) return true;
        return false;
    }
    /**
     * 去掉一层根目录
     * @param r
     * @throws XMLStreamException
     * @throws ServiceException
     */
    private static void removeDataStartElement(XMLEventReader r) throws XMLStreamException {
        while (r.hasNext()) {
            XMLEvent e = r.nextEvent();
            if (e.isStartElement()) {
                return;
            }
        }
        throw new SystemException("XML不包含任何标签");
    }
    
    public static void main(String[] args) throws Exception{
       String strs = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<SOAP-ENV:Body>" + "<m:sendSMSRequest xmlns:m=\"http://sms.protocol.intf.tisson.cn\">"
                + "<accountAuth>" + "<account>String</account>" + "<password><![CDATA[sdfsdfs<ads>]]></password>"
                + "<hashCode>String</hashCode>" + "</accountAuth>" + "<sendNum>String</sendNum>"
                + "<schdDate>String</schdDate>" + "<recvNumArray>" + "<recvNum>String</recvNum>" + "</recvNumArray>"
                + "<content>String</content>" + "<receiptFlag>0</receiptFlag>" + "<busiCode>String</busiCode>"
                + "</m:sendSMSRequest>" + "</SOAP-ENV:Body>" + "</SOAP-ENV:Envelope>";
        DataModel obj = DataXmlParser.xmlToObject(strs);
        System.out.println(obj);
        String strs2="<xml>"+
                "<ToUserName><![CDATA[toUser]]></ToUserName>"+
                "<FromUserName><![CDATA[fromUser]]></FromUserName>"+
                "<CreateTime>1348831860</CreateTime>"+
                "<MsgType><![CDATA[sendSMS]]></MsgType>"+
                "<Content><![CDATA[this is a test]]></Content>"+
                "<tstaaa><hashCode>String</hashCode><sendNum>String</sendNum></tstaaa>"+
                "<MsgId>1234567890123456</MsgId>"+
                "<MsgId>1234567890123456</MsgId>"+
                "</xml>";
        System.out.println("*********************************************8");
        DataModel obj2 = DataXmlParser.xmlToObject(strs2);
        System.out.println(obj2);
    }
}
