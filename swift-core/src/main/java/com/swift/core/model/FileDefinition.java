/**
 * @(#)FileDefinition.java 1.0.0 Mar 2, 2016 2:48:42 PM 
 *  
 * Copyright (c) 2001-2016 GuangDong Eshore Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.core.model;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swift.core.model.data.AbstractBeanDataModel;

/**
 * 上传的文件描述
 * 
 * @author Avery Xiao
 * @version 1.0.0
 * @date Mar 2, 2016 2:48:42 PM
 */
public class FileDefinition extends AbstractBeanDataModel {

	/**
	 * Content Type
	 */
	private String contentType;
	/**
	 * 文件名（全路径）
	 */
	private String fileName;
	/**
	 * 文件大小
	 */
	private long size = -1;
	/**
	 * 文件流
	 */
	@JsonIgnore
	private InputStream inputStream;
	/**
	 * 回调
	 */
	@JsonIgnore
	private FileOperationCompletedCallback operationCallback;

	/**
	 * 回调
	 * 
	 * @author Avery Xiao
	 * @version 1.0.0
	 * @date Mar 4, 2016 3:17:27 PM
	 */
	public interface FileOperationCompletedCallback {

		void callback(String fileName);
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		if (inputStream != null) {
			try {
				size = inputStream.available();
			} catch (IOException ex) {
			}
		}
		this.inputStream = inputStream;
	}

	/**
	 * @return the operationCallback
	 */
	public FileOperationCompletedCallback getOperationCallback() {
		return operationCallback;
	}

	/**
	 * @param operationCallback
	 *            the operationCallback to set
	 */
	public void setOperationCallback(FileOperationCompletedCallback operationCallback) {
		this.operationCallback = operationCallback;
	}
}
