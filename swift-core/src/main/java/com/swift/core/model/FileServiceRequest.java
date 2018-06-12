/*
 * @(#)FileServiceRequest.java   1.0  2018年6月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model;

import java.util.List;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月12日
 */
public class FileServiceRequest extends ServiceRequest{
    /**
     * 文件列表
     */
    private List<FileDefinition> files;
    

    /**
     * @return the files
     */
    public List<FileDefinition> getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(List<FileDefinition> files) {
        this.files = files;
    }
}
