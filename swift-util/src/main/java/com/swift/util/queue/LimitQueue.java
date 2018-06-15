/*
 * @(#)LimitQueue.java   1.0  2018年6月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月15日
 */
public class LimitQueue<E> implements BlockingQueue<E>{

   //队列长度  
    private int limit;  
      
    private ArrayBlockingQueue<E> queue;
    
      
    public LimitQueue(int limit){  
        this.limit = limit;  
        queue = new ArrayBlockingQueue<E>(this.limit); 
    }  
      
    /** 
     * 入队 
     * @param e 
     */  
    @Override  
    public boolean offer(E e){  
        synchronized (this) {
            if(queue.size() >= limit){  
                //如果超出长度,入队时,先出队  
                queue.poll();  
            }
            return queue.offer(e);  
        } 
    }  
      
    /** 
     * 出队 
     * @return 
     */  
    @Override  
    public E poll() {  
        return queue.poll();  
    }
    
    /** 
     * 获取限制大小 
     * @return 
     */  
    public int getLimit(){  
        return limit;  
    }  
  
    @Override  
    public boolean add(E e) {  
        synchronized (this) {
            if(queue.size() >= limit){  
                //如果超出长度,入队时,先出队  
                queue.poll();  
            }
            return queue.add(e); 
        }
    }  
  
    @Override  
    public E element() {  
        return queue.element();  
    }  
  
    @Override  
    public E peek() {  
        return queue.peek();  
    }  
  
    @Override  
    public boolean isEmpty() {  
        return queue.isEmpty();
    }  
  
    @Override  
    public int size() {  
        return queue.size();  
    }  
  
    @Override  
    public E remove() {  
        return queue.remove();  
    }  
  
    @Override  
    public boolean addAll(Collection<? extends E> c) {  
        if(c==null || c.isEmpty()) return true;
        synchronized (this) {
            if(c.size()>limit) {
                List<E> list = new ArrayList<E>();
                int i=0;
                for(E e:c) {
                    if(i>=c.size()-limit) {
                        list.add(e); 
                    }else {
                        i=i+1;
                    }
                }
                c = list;
            }
            int delNum = queue.size()+c.size()-limit;
            if(delNum>0){  
                for(int i=0;i<delNum;i++) {
                    queue.poll(); 
                }
            }
            return queue.addAll(c);  
        }
    }  
  
    @Override  
    public void clear() {  
        queue.clear();  
    }  
  
    @Override  
    public boolean contains(Object o) {  
        return queue.contains(o);  
    }  
  
    @Override  
    public boolean containsAll(Collection<?> c) {  
        return queue.containsAll(c);  
    }  
  
    @Override  
    public Iterator<E> iterator() {  
        return queue.iterator();  
    }  
  
    @Override  
    public boolean remove(Object o) {  
        return queue.remove(o);  
    }  
  
    @Override  
    public boolean removeAll(Collection<?> c) {  
        return queue.removeAll(c);  
    }  
  
    @Override  
    public boolean retainAll(Collection<?> c) {  
        return queue.retainAll(c);  
    }  
  
    @Override  
    public Object[] toArray() {  
        return queue.toArray();  
    }  
  
    @Override  
    public <T> T[] toArray(T[] a) {  
        return queue.toArray(a);  
    }

    /** 
     * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
     */
    @Override
    public void put(E e) throws InterruptedException {
        synchronized (this) {
            if(queue.size() >= limit){  
                //如果超出长度,入队时,先出队  
                queue.poll();  
            }
            queue.put(e);
        }
    }

    /** 
     * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        synchronized (this) {
            if(queue.size() >= limit){  
                //如果超出长度,入队时,先出队  
                queue.poll();  
            }
            return queue.offer(e, timeout, unit);
        }
    }

    /** 
     * @see java.util.concurrent.BlockingQueue#take()
     */
    @Override
    public E take() throws InterruptedException {
        return queue.take();
    }

    /** 
     * @see java.util.concurrent.BlockingQueue#poll(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    /** 
     * @see java.util.concurrent.BlockingQueue#remainingCapacity()
     */
    @Override
    public int remainingCapacity() {
        return queue.remainingCapacity();
    }

    /** 
     * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection)
     */
    @Override
    public int drainTo(Collection<? super E> c) {
        return queue.drainTo(c);
    }

    /** 
     * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection, int)
     */
    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return queue.drainTo(c, maxElements);
    }  
  
}  

