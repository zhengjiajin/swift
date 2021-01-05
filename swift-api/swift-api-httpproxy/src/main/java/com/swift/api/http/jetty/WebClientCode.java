/*
* @(#)WebEncode.java 1.0 2014-1-21
*
* Copyright (c) 2014-2020. All Rights Reserved. GuangDong Eshore Technology Company LTD.
*/
package com.swift.api.http.jetty;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.eclipse.jetty.client.util.InputStreamContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.core.api.rpc.ClientEngine.CallBackEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.exception.ResultCode;
import com.swift.exception.extend.SystemException;

/**
 * WEB的编解密
 *
 * @author jiajin
 * @version 1.0 2014-1-21
 */
public abstract class WebClientCode {
    private final static Logger log = LoggerFactory.getLogger(WebClientCode.class);

    /**
     * 编码请求
     *
     * @param ParRequest
     *            要发送的请求
     * @param exchange
     *            编码内容存放
     * @throws ParException
     */
    public abstract HttpContentExchange encode(ServiceRequest req);

    /**
     * 响应解码
     *
     * @param response
     *            解码内容
     * @param Response
     *            解码后存放
     * @throws Exception
     */
    public abstract ServiceResponse decode(String response);

    /**
     * 创建接口响应
     *
     * @param response
     * @return
     */
    protected abstract Object createResponse(ServiceResponse response);

    /**
     *
     * Web的返回参数
     *
     * @author jiajin
     * @version 1.0 2014-5-13
     */
    public class HttpContentExchange extends BufferingResponseListener {
        private URI URI;
        private String URL;
        private HttpMethod method = HttpMethod.GET;
        private int timeout = 0;
        private Map<String, String> requestHeader = new HashMap<String, String>();
        private InputStreamContentProvider requestContent;
        private ServiceRequest request;
        private HttpClient httpClient;
        @SuppressWarnings("rawtypes")
        private CallBackEngine callBack;

        public ServiceResponse getServerResponse(Response response, byte[] content) {
            ServiceResponse res = null;
            int status = response.getStatus();
            String rsp = new String(content);
            log.info("收到Http响应 >> [请求体]：" + request.toString());
            log.info("收到Http响应 >> " + rsp);
            if (status == 200) {
                // 设cookit
                HttpFields field = response.getHeaders();
                for (String name : field.getFieldNamesCollection()) {
                    log.info("收到响应参数：" + name + ":" + field.get(name));
                    if (name.indexOf("Set-") == 0) {
                        String value = field.get(name);
                        httpClient.getCookieStore().add(getURI(),
                            new HttpCookie(name.replace("Set-", ""), value.substring(0, value.indexOf(";"))));
                    }
                }
                // 小程序二维码API返回为image/jpeg格式数据流
                // 采用Base64编码转换为字符串
                if (field.get("Content-Type") != null && field.get("Content-Type").equals("image/jpeg")) {
                    rsp = Base64.getEncoder().encodeToString(content);
                }
                try {
                    res = decode(rsp);
                    res.setRequest(request);
                    res.setResponseTime(System.currentTimeMillis());
                } catch (Exception ex) {
                    return errorRes(ResultCode.PROTOCOL_ERROR, "返回包解释错误");
                }
            } else {
                log.error("收到Http错误响应：" + status + ":" + rsp);
                return errorRes(ResultCode.OTHER_SYS_ERROR, rsp);
            }
            return res;
        }

        public void setRequest(ServiceRequest request) {
            this.request = request;
        }

        private ServiceResponse errorRes(int erroCode, String errorMsg) {
            ServiceResponse rsp = new ServiceResponse();
            rsp.setResultCode(erroCode);
            rsp.setReason(errorMsg);
            rsp.setRequest(request);
            return rsp;
        }

        /**
         * @return the httpClient
         */
        public HttpClient getHttpClient() {
            return httpClient;
        }

        /**
         * @param httpClient
         *            the httpClient to set
         */
        public void setHttpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
        }

        /**
         * @param uRL
         *            the uRL to set
         */
        public void setURL(String uRL) {
            this.URL = uRL;
            try {
                this.URI = new URI(uRL);
            } catch (URISyntaxException e) {
                throw new SystemException("url错误:" + uRL, e);
            }
        }

        /**
         * @return the uRL
         */
        public String getURL() {
            return URL;
        }

        /**
         * @return the uRI
         */
        public URI getURI() {
            return URI;
        }

        /**
         * @param uRI
         *            the uRI to set
         */
        public void setURI(URI uRI) {
            URI = uRI;
        }

        /**
         * @return the method
         */
        public HttpMethod getMethod() {
            return method;
        }

        /**
         * @param method
         *            the method to set
         */
        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        /**
         * @return the requestHeader
         */
        public Map<String, String> getRequestHeader() {
            return requestHeader;
        }

        /**
         * @param requestHeader
         *            the requestHeader to set
         */
        public void setRequestHeader(Map<String, String> requestHeader) {
            this.requestHeader = requestHeader;
        }

        public void addRequestHeader(String name, String value) {
            this.requestHeader.put(name, value);
        }

        /**
         * @return the requestContent
         */
        public InputStreamContentProvider getRequestContent() {
            return requestContent;
        }

        /**
         * @param requestContent
         *            the requestContent to set
         */
        public void setRequestContent(InputStreamContentProvider requestContent) {
            this.requestContent = requestContent;
        }

        public void setRequestContent(InputStream requestContent) {
            this.requestContent = new InputStreamContentProvider(requestContent);
        }

        public void setRequestContent(ByteArrayInputStream requestContent) {
            this.requestContent = new InputStreamContentProvider(requestContent);
        }

        public void setRequestContent(byte[] requestContent) {
            setRequestContent(new ByteArrayInputStream(requestContent));
        }

        /**
         * @return the request
         */
        public ServiceRequest getRequest() {
            return request;
        }

        /**
         * @return the callBack
         */
        @SuppressWarnings("rawtypes")
        public CallBackEngine getCallBack() {
            return callBack;
        }

        /**
         * @param callBack
         *            the callBack to set
         */
        @SuppressWarnings("rawtypes")
        public void setCallBack(CallBackEngine callBack) {
            this.callBack = callBack;
        }

        @SuppressWarnings("unchecked")
        private void callBack(ServiceResponse res) {
            if (this.callBack != null) this.callBack.receiveResponse(createResponse(res));
        }

        /**
         * @return the timeout
         */
        public int getTimeout() {
            return timeout;
        }

        /**
         * @param timeout
         *            the timeout to set
         */
        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        /**
         * @see org.eclipse.jetty.client.api.Response.CompleteListener#onComplete(org.eclipse.jetty.client.api.Result)
         */
        @Override
        public void onComplete(Result result) {
            callBack(getServerResponse(result.getResponse(), super.getContent()));
        }

        @Override
        public void onFailure(Response response, Throwable failure) {
            log.error("Http错误onFailure：" + response.getStatus() + "," + response.getReason() + ":", failure);
        }
    }
}
