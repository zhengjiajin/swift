/*
 * @(#)ParamLengthDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamLength;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamLengthDef extends ConstraintDef<ParamLength> {
    private static ParamLengthDef def = new ParamLengthDef();

    private ParamLengthDef() {
    }

    public static ParamLengthDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamLength anno) {
        if(obj==null) return true;
        int length = TypeUtil.getLength(obj);
        if(length>anno.anno().max()) return false;
        if(length<anno.anno().min()) return false;
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamLength anno) {
        String msg = getLocalMessage(anno.anno().message());
        return msg.replace("{min}", TypeUtil.toString(anno.anno().min())).replace("{max}", TypeUtil.toString(anno.anno().max()));
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamLength anno) {
        return anno.param();
    }

}
