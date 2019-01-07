/*
 * @(#)ParamRangeDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamRange;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamRangeDef extends ConstraintDef<ParamRange> {
    private static ParamRangeDef def = new ParamRangeDef();

    private ParamRangeDef() {
    }

    public static ParamRangeDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamRange anno) {
        if(obj==null) return true;
        if(TypeUtil.isNumber(obj)) {
            if(TypeUtil.toNumber(obj).doubleValue()<anno.min()) return false;
            if(TypeUtil.toNumber(obj).doubleValue()>anno.max()) return false;
        }else {
            int length = TypeUtil.getLength(obj);
            if(length<anno.min()) return false;
            if(length>anno.max()) return false;
        }
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamRange anno) {
        String msg = getLocalMessage(anno.message());
        return msg.replace("{min}", TypeUtil.toString(anno.min())).replace("{max}", TypeUtil.toString(anno.max()));
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamRange anno) {
        return anno.param();
    }

}
