/*
 * @(#)ParamDecimalMaxDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamDecimalMax;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamDecimalMaxDef extends ConstraintDef<ParamDecimalMax> {

    private static ParamDecimalMaxDef def = new ParamDecimalMaxDef();

    private ParamDecimalMaxDef() {
    }

    public static ParamDecimalMaxDef getDef() {

        return def;
    }


    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamDecimalMax anno) {
        if(obj==null) return true;
        if(TypeUtil.isNumber(obj)) {
            if(TypeUtil.toNumber(obj).doubleValue()>anno.max()) return false;
        }else {
            int length = TypeUtil.getLength(obj);
            if(length>anno.max()) return false;
        }
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamDecimalMax anno) {
        String msg = getLocalMessage(anno.message());
        return msg.replace("{max}", TypeUtil.toString(anno.max()));
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamDecimalMax anno) {
        return anno.param();
    }

}
