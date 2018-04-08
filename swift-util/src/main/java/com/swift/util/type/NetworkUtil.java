/**
 * @(#)NetworkUtil.java 1.0.0 Dec 21, 2015 3:44:08 PM 
 *  
 * Copyright (c) 2001-2015 GuangDong Eshore Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.util.type;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络相关的工具类
 * 
 * @author zhengjiajin
 * @version 1.0.0
 * @date Dec 21, 2015 3:44:08 PM
 */
public class NetworkUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtil.class);

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
