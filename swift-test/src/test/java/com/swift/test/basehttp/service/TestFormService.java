/*
 * @(#)TestFormService.java   1.0  2018年6月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.basehttp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swift.core.model.FileDefinition;
import com.swift.core.model.FileServiceRequest;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.SimpleInterface;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月12日
 */
@Service("testFormService")
public class TestFormService implements SimpleInterface{

    /** 
     * @see com.swift.core.service.SimpleInterface#doService(com.swift.core.model.ServiceRequest)
     */
    @Override
    public DataModel doService(ServiceRequest req) {
        FileServiceRequest filereq = (FileServiceRequest)req;
        List<FileDefinition> files = filereq.getFiles();
        if(TypeUtil.isNotNull(files)) {
            System.out.println("*********:"+files.size());
            for(FileDefinition fi:files) {
                System.out.println("fileName:"+fi.getFileName());
                System.out.println("fileName-getContentType:"+fi.getContentType());
                System.out.println("fileName-getSize:"+fi.getSize());
            }
        }
        return req.getData();
    }

}
