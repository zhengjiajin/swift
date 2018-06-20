/*
 * @(#)ParamAssertTrueDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamAssertTrue;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamAssertTrueDef extends ConstraintDef<ParamAssertTrue> {
    private static ParamAssertTrueDef def = new ParamAssertTrueDef();

    private ParamAssertTrueDef() {
    }

    public static ParamAssertTrueDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamAssertTrue anno) {
        return TypeUtil.isTrue(obj);
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamAssertTrue anno) {
        return getLocalMessage(anno.anno().message());
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamAssertTrue anno) {
        return anno.param();
    }


}
