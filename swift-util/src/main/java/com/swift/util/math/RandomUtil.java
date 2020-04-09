/*
 * @(#)RandomUtil.java   1.0  2017年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.math;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import com.swift.util.type.NetworkUtil;
import com.swift.util.type.TypeUtil;

/**
 * 得到随机值方法
 * @author 郑家锦
 * @version 1.0 2017年4月12日
 */
public class RandomUtil {
    private static ThreadLocalRandom random = ThreadLocalRandom.current();
    
    private static AtomicLong atomicLong = new AtomicLong(0);
    /**
     * 随机拆分算法,把一个值随机拆成N个值(直到拆完)
     * @param num
     * @return
     */
    public static List<Integer> randomSpilt(int num,int max){
        List<Integer> list = new LinkedList<Integer>();
        randomSpilt(list, num,max);
        return list;
    }
    
    public static void randomSpilt(List<Integer> list,int num,int max){
        int i = random.nextInt(1, max+1);
        list.add(i);
        int f = num-i;
        if(f<=0) return;
        randomSpilt(list, f,max);
    }
    
    
    /**
     * 得一个率里得出是否中奖
     * @param rate 0~1
     * @return
     */
    public static boolean isWinning(double rate){
        double value = random.nextDouble(Double.MIN_VALUE, 1);
        return rate>=value;
    }
    
    private static String IP = NetworkUtil.getHostAddress(); 
    
    private static String ipend = String.format("%03d", TypeUtil.toInt(IP.substring(IP.lastIndexOf(".")+1)));
    
    private static String pidname = ManagementFactory.getRuntimeMXBean().getName();
    
    private static String pid = pidname.substring(0, pidname.indexOf("@"));
    
    public static String createReqId(){
        //IP后三位+秒+自增
       long time = System.currentTimeMillis()/1000;
       String ato = String.format("%04d",atomicLong.incrementAndGet()%10000);
       long value = Long.valueOf(time+ato);
       long stvalue = Long.valueOf(ipend+pid);
       return Long.toString(stvalue,Character.MAX_RADIX)+Long.toString(value,Character.MAX_RADIX);
    }
    
    /**
     * 产生随机int
     * min 包含
     * max 不含
     * @return int
     */
    public static int createCode(int min,int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
    
}
