/*
 * @(#)ParamNotNullDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamNotNull;
import com.swift.core.validator.core.data.ConstraintDef;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamNotNullDef extends ConstraintDef<ParamNotNull> {
    private static ParamNotNullDef def = new ParamNotNullDef();

    private ParamNotNullDef() {
    }

    public static ParamNotNullDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamNotNull anno) {
        if(obj==null) return false;
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamNotNull anno) {
        return getLocalMessage(anno.message());
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamNotNull anno) {
        return anno.param();
    }

}
