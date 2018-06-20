/*
 * @(#)ParamAssertFalseDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamAssertFalse;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamAssertFalseDef extends ConstraintDef<ParamAssertFalse>{
    
    private static ParamAssertFalseDef def = new ParamAssertFalseDef();
    
    private ParamAssertFalseDef() {
    }
    
    public static ParamAssertFalseDef getDef() {
        return def;
    }
    
    @Override
    protected boolean checkObj( Object obj , ParamAssertFalse anno) {
        return !TypeUtil.isTrue(obj);
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamAssertFalse anno) {
        return getLocalMessage(anno.anno().message());
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamAssertFalse anno) {
        return anno.param();
    }

}
