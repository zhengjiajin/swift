/*
 * @(#)SystemEnvServiceImpl.java   1.0  2018年7月13日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swift.test.db.jpa.SystemEnvRepository;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月13日
 */
@Service
public class SystemEnvServiceImpl {
    @Autowired(required=false)
    private SystemEnvRepository systemEnvRepository;
    
    @Transactional
    public void save(SystemEnv systemEnv) {
        systemEnvRepository.save(systemEnv);
    }
}
