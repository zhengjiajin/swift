/*
 * @(#)ParamMinDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamMin;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamMinDef extends ConstraintDef<ParamMin> {
    private static ParamMinDef def = new ParamMinDef();

    private ParamMinDef() {
    }

    public static ParamMinDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamMin anno) {
        if(obj==null) return true;
        if(TypeUtil.isNumber(obj)) {
            if(TypeUtil.toNumber(obj).doubleValue()<anno.anno().value()) return false;
        }else {
            int length = TypeUtil.getLength(obj);
            if(length<anno.anno().value()) return false;
        }
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamMin anno) {
        String msg = getLocalMessage(anno.anno().message());
        return msg.replace("{value}", TypeUtil.toString(anno.anno().value()));
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamMin anno) {
        return anno.param();
    }

}
