/*
 * @(#)PicUtil.java   1.0  2018年8月1日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.layout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.swift.exception.SwiftRuntimeException;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月1日
 */
public class PicUtil {
    
    public static byte[] createCodePic(int width,int height,String code) {
        BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        //相当于得到一支笔
        Graphics graphics=image.getGraphics();
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, width, height);
        //设置画笔的颜色
        graphics.setColor(Color.red);
        //设置字体的属性
        graphics.setFont(new Font(Font.SERIF, Font.TYPE1_FONT, 25));
        //将随机字符画到图片上
        graphics.drawString(code, 5, 18);
        try {
            ByteArrayOutputStream outputString = new ByteArrayOutputStream();
            ImageIO.write(image,"png",outputString);
            return outputString.toByteArray();
        } catch (IOException e) {
            throw new SwiftRuntimeException("图片生成失败");
        }
    }
    
    public static void main(String[] args) {
        byte[] a = createCodePic(70, 25, "6548");
        String str = Base64.getEncoder().encodeToString(a);
        System.out.println(str);
        
    }
}
