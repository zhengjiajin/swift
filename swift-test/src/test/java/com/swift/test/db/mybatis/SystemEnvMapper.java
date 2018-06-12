/*
 * @(#)SystemEnvMapper.java   1.0  2018年6月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db.mybatis;

import com.swift.dao.db.mybatis.MybatisMapper;
import com.swift.test.db.SystemEnv;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月5日
 */
@MybatisMapper
public interface SystemEnvMapper {

    public SystemEnv get(String keyId);
}
