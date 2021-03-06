/*
 * @(#)ParamSizeDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamSize;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamSizeDef extends ConstraintDef<ParamSize> {
    private static ParamSizeDef def = new ParamSizeDef();

    private ParamSizeDef() {
    }

    public static ParamSizeDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamSize anno) {
        if(obj==null) return true;
        int length = TypeUtil.getLength(obj);
        if(length>anno.max()) return false;
        if(length<anno.min()) return false;
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamSize anno) {
        String msg = getLocalMessage(anno.message());
        return msg.replace("{min}", TypeUtil.toString(anno.min())).replace("{max}", TypeUtil.toString(anno.max()));
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamSize anno) {
        return anno.param();
    }

}
