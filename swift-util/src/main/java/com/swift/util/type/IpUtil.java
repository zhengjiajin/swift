package com.swift.util.type;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);
	/** 
	 * 获取客户端IP地址   
	 * @param 
	 * @return String    
	 * @throws 
	 */  
	public static String getIpAddr(HttpServletRequest request) {   
	       String ip = request.getHeader("x-forwarded-for");   
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("Proxy-Client-IP");   
	       }   
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("WL-Proxy-Client-IP");   
	       }   
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getRemoteAddr();   
	           if(ip.equals("127.0.0.1")){     
	               //根据网卡取本机配置的IP     
	               InetAddress inet=null;     
	               try {     
	                   inet = InetAddress.getLocalHost();     
	               } catch (UnknownHostException e) {     
	                   e.printStackTrace();     
	               }     
	               ip= inet.getHostAddress();     
	           }  
	       }   
	       // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
	       if(ip != null && ip.length() > 15){    
	           if(ip.indexOf(",")>0){     
	               ip = ip.substring(0,ip.indexOf(","));     
	           }     
	       }     
	       return ip;   
	}  
	
	/**
	 * 获取域名对应IP
	 * @param domain
	 * @return
	 */
    public static String domainToIp(String domain) {
        try {
            InetAddress address = InetAddress.getByName(domain);
            return address.getHostAddress().toString();
        } catch (UnknownHostException e) {
            return null;
        }
    }
    
    /**
	 * 得到本机局域网IP
	 * @return
	 */
	public static String getHostAddress() {
		return getHostAddress(getLocalAddress());
	}

	public static String getHostAddress(byte[] addr) {
		try {
			return InetAddress.getByAddress(addr).getHostAddress();
		} catch (UnknownHostException ex) {
			LOGGER.error("Unexpected exception: ", ex);
		}
		return InetAddress.getLoopbackAddress().getHostAddress();
	}

	public static byte[] getLocalAddress() {
		try {
			for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces
					.hasMoreElements();) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()
						|| networkInterface.isPointToPoint()) {
					continue;
				}
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					if (addr instanceof Inet6Address) {
						continue;
					}
					return addr.getAddress();
				}
			}
		} catch (IOException ex) {
			LOGGER.error("Unexpected exception: ", ex);
		}
		return InetAddress.getLoopbackAddress().getAddress();
	}

	/**
	 * Given a string representation of a host, return its ip address in textual
	 * presentation.
	 * 
	 * @param name
	 *            a string representation of a host: either a textual
	 *            representation its IP address or its host name
	 * @return its IP address in the string format
	 */
	public static String normalizeHostName(String name) {
		try {
			return InetAddress.getByName(name).getHostAddress();
		} catch (UnknownHostException ex) {
			LOGGER.error("Unexpected exception: ", ex);
			return name;
		}
	}
	
	public static void main(String[] args) {
	    System.out.println(getHostAddress());
    }
}
