/*
 * @(#)ChangeFuture.java   1.0  2020年9月17日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.api.asyn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import com.swift.exception.extend.SystemException;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年9月17日
 */
public class ChangeFuture<T,V> implements Future<V> {

    private Future<T> oldFuture;
    
    private Function<T,V> changeFucntion;
    
    public ChangeFuture(Future<T> oldFuture,Function<T,V> changeFucntion) {
        if(oldFuture==null || changeFucntion==null) throw new SystemException("ChangeFuture pram is null");
        this.oldFuture=oldFuture;
        this.changeFucntion=changeFucntion;
    }
    /** 
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return oldFuture.cancel(mayInterruptIfRunning);
    }

    /** 
     * @see java.util.concurrent.Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return oldFuture.isCancelled();
    }

    /** 
     * @see java.util.concurrent.Future#isDone()
     */
    @Override
    public boolean isDone() {
        return oldFuture.isDone();
    }

    /** 
     * @see java.util.concurrent.Future#get()
     */
    @Override
    public V get() throws InterruptedException, ExecutionException {
        return changeFucntion.apply(oldFuture.get());
    }

    /** 
     * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return changeFucntion.apply(oldFuture.get(timeout, unit));
    }

}
