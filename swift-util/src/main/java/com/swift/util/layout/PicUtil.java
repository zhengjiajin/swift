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
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
    
    public static byte[] createQrCodePic(int width,int height,String contents) {
        return createQrCodePic(width, height, contents, "png");
    }
    
    public static byte[] createQrCodePic(int width,int height,String contents,String format) {
        try {
            Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //编码
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); //容错率
            hints.put(EncodeHintType.MARGIN, 0);  //二维码边框宽度，这里文档说设置0-4，但是设置后没有效果，不知原因，
            BitMatrix bm = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height,hints);
            ByteArrayOutputStream outputString = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bm, format, outputString);
            return outputString.toByteArray();
        } catch (Exception e) {
            throw new SwiftRuntimeException("二维码生成失败");
        }
    }
    
    public static void main(String[] args) {
        byte[] a = createCodePic(70, 25, "6548");
        String str = Base64.getEncoder().encodeToString(a);
        System.out.println(str);
        
    }
}
