/*
 * @(#)TestViModelService.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.vali.service;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.SimpleInterface;
import com.swift.core.validator.DataValidator;
import com.swift.core.validator.annotation.ParamAssertFalse;
import com.swift.core.validator.annotation.ParamAssertTrue;
import com.swift.core.validator.annotation.ParamDecimalMin;
import com.swift.core.validator.annotation.ParamEmail;
import com.swift.core.validator.annotation.ParamFuture;
import com.swift.core.validator.annotation.ParamLength;
import com.swift.core.validator.annotation.ParamMax;
import com.swift.core.validator.annotation.ParamMin;
import com.swift.core.validator.annotation.ParamNotNull;
import com.swift.core.validator.annotation.ParamPast;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
@Service("testViModelService")
public class TestViModelService implements SimpleInterface{

    /** 
     * @see com.swift.core.service.SimpleInterface#doService(com.swift.core.model.ServiceRequest)
     */
    @Override
    @DataValidator(Null= {},
                   NotNull= {@ParamNotNull(param="testAbc",anno=@NotNull(message="testAbc不能为空")),@ParamNotNull(param="bbb.ccc",anno=@NotNull)},
                   AssertTrue= {@ParamAssertTrue(param="assertTrue",anno=@AssertTrue(message="AssertTrue不能为false"))},
                   AssertFalse= {@ParamAssertFalse(param="assertFalse",anno=@AssertFalse(message="assertFalse不能为true"))},
                   Min= {@ParamMin(param="Min",anno=@Min(10))},
                   Max= {@ParamMax(param="Max",anno=@Max(11))},
                   DecimalMin= {@ParamDecimalMin(param="DecimalMin",anno=@DecimalMin("11.5"))},
                   Size= {},
                   Digits= {},
                   Past= {@ParamPast(param="Past",anno=@Past)},
                   Future= {@ParamFuture(param="Future",anno=@Future)},
                   Pattern= {},
                   Email= {@ParamEmail(param="Email",anno=@Email)},
                   Length= {@ParamLength(param="Length",anno=@Length(min=2,max=10))},
                   NotEmpty= {},
                   Range= {}
                   )
    public DataModel doService(ServiceRequest req) {
        System.out.println(req.getData());
        return req.getData();
    }

}
