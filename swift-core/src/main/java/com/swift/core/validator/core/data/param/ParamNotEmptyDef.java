/*
 * @(#)ParamNotEmptyDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamNotEmpty;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamNotEmptyDef extends ConstraintDef<ParamNotEmpty> {
    private static ParamNotEmptyDef def = new ParamNotEmptyDef();

    private ParamNotEmptyDef() {
    }

    public static ParamNotEmptyDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamNotEmpty anno) {
        if(TypeUtil.isNull(obj)) return false;
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamNotEmpty anno) {
        return getLocalMessage(anno.anno().message());
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamNotEmpty anno) {
        return anno.param();
    }

}
