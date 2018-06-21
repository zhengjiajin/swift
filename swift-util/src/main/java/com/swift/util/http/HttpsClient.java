/*
 * @(#)HttpsClient.java   1.0  2018年6月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月21日
 */
public class HttpsClient {
    /**
     * 获取url请求后返回的内容
     * 
     * @param requestUrl
     * @return
     */
    public static String doGet(String requestUrl) {
        String result = "";
        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return false;
                }
            });
            InputStream in = httpsConn.getInputStream();
            result = getContent(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected static String getContent(InputStream in) throws UnsupportedEncodingException, IOException {
        InputStreamReader reader = new InputStreamReader(in, "UTF-8");
        BufferedReader bufReader = new BufferedReader(reader);
        String tmp = null;
        StringBuilder sb = new StringBuilder();
        while ((tmp = bufReader.readLine()) != null) {
            sb.append(tmp);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String url = "https://www.baidu.com/";
        System.out.println(doGet(url));
    }
}
