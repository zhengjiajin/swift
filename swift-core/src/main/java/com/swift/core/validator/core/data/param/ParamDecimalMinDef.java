/*
 * @(#)ParamDecimalMinDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamDecimalMin;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamDecimalMinDef extends ConstraintDef<ParamDecimalMin> {

    private static ParamDecimalMinDef def = new ParamDecimalMinDef();

    private ParamDecimalMinDef() {
    }

    public static ParamDecimalMinDef getDef() {

        return def;
    }

    /**
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamDecimalMin anno) {
        if(obj==null) return true;
        if(TypeUtil.isNumber(obj)) {
            if(TypeUtil.toNumber(obj).doubleValue()<TypeUtil.toDouble(anno.anno().value(), 0)) return false;
        }else {
            int length = TypeUtil.getLength(obj);
            if(length<TypeUtil.toDouble(anno.anno().value(), 0)) return false;
        }
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamDecimalMin anno) {
        String msg = getLocalMessage(anno.anno().message());
        return msg.replace("{value}", TypeUtil.toString(anno.anno().value()));
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamDecimalMin anno) {
        return anno.param();
    }

}
