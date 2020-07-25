/*
 * @(#)LifeCycle.java   1.0  2011-8-8
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.server;

/**
 * 
 * 生命周期定义。实现该接口的类支持其定义的各个操作，如启动、停止。
 * 
 * @author jiajin
 * @version 1.0  2011-8-16
 */
public interface LifeCycle {

	public void start(int port);
	
	public boolean isStarted();
	
	public void stop();
}
