package com.swift.core.filter.impl;


import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swift.core.filter.RequestFilter;
import com.swift.core.filter.annotation.NoSqlVal;
import com.swift.core.model.ServiceRequest;
import com.swift.core.service.ReqInterfaceFactory;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.util.bean.AnnotationUtil;

/**
 * 添加说明 
 * @author kyj
 * @version 1.0 2015年8月20日 防止SQL注入过滤器
 */
@Service
public class SqlInjectionFilter implements RequestFilter {
    
    private final static Logger log = LoggerFactory.getLogger(SqlInjectionFilter.class);
	
	public static final String reg = //"(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"  + 
            "(\\b(update|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
	
	static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	@Override
	public void doFilter(ServiceRequest req) throws ServiceException {
	    String method = req.getMethod();
        Object obj = ReqInterfaceFactory.getInterfaceTrueObj(method, req.getInterfaceVersion());
        if (obj == null) throw new ServiceException(ResultCode.NO_METHOD, "没有找到方法对应的OBJ");
        NoSqlVal noSqlVal = AnnotationUtil.getAnnotation(obj.getClass(), NoSqlVal.class);
	    if(noSqlVal!=null) return;
	    
		String sql = req.getData().toString();
        if (sqlValidate(sql)) {
            String msg = "您发送请求中的参数中含有非法字符";
            log.error(msg);
            throw new ServiceException(ResultCode.ERROR_PARAMETER, msg);
        } 
	}

	//效验
    protected static boolean sqlValidate(String str) {
        if (sqlPattern.matcher(str).find()) {
            return true;  
        }  
        return false;
    }
}
