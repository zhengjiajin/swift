/*
 * @(#)SystemEnvJdbcImpl.java   1.0  2018年7月13日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.dao.db.springjdbc.Jdbc;
import com.swift.test.db.SystemEnv;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月13日
 */
@Component
public class SystemEnvJdbcImpl {

    @Autowired  
    private Jdbc jdbc;
    
    
    public void add(SystemEnv systemEnv) {
        jdbc.update("insert into System_Env(key_id,value_Content) values('"+systemEnv.getKeyId()+"','"+systemEnv.getValueContent()+"')");
    }
}
