/*
 * @(#)DateUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.swift.util.type.TypeUtil;

/**
 * 日期时间处理类库
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
    private static final String[] PARSE_PATTERNS = { "yyyyMM", "yyyy-MM", "yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss" };

    public static final String DEF_DATE_TYPE = "yyyy-MM-dd HH:mm:ss";

    public static Date toDate(Object obj) {
        if (TypeUtil.isNotNull(obj)) {
            if (obj instanceof Date) {
                return (Date) obj;
            } else {
                return parseDate(TypeUtil.toString(obj));
            }
        }
        return null;
    }

    /**
     * 返回格式化的日期
     * 
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        if (date == null) return null;
        if (StringUtils.isEmpty(format)) format = DEF_DATE_TYPE;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public static String formatDate(Date date) {
        return formatDate(date, null);
    }

    // 返回日期
    public static Date parseDate(String dateStr) {
        try {
            if(TypeUtil.isNumber(dateStr)) return new Date(TypeUtil.toLong(dateStr));
            return parseDate(dateStr, PARSE_PATTERNS);
        } catch (ParseException e) {
            throw new RuntimeException("解析日期出错:" + dateStr + e.getMessage());
        }
    }
    
    public static Date getMonthBegin(Date date) {
        if (date != null) {
            return parseDate(formatDate(date, "yyyy-MM") + "-01");
        }
        return null;
    }

    public static Date getMonthEnd(Date date) {
        if (date == null) {
            return null;
        }
        Date tmp = getMonthBegin(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tmp);
        calendar.add(2, 1);
        calendar.add(6, -1);
        return calendar.getTime();
    }

    public static Date getWeekBegin(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(2);
        calendar.set(7, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    public static Date getWeekEnd(Date date) {
        if (date == null) {
            return null;
        }
        Date weekBegin = getWeekBegin(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(weekBegin);
        calendar.add(6, 6);
        return calendar.getTime();
    }

    public static boolean before(Date date1, Date date2) {
        if ((date1 == null) || (date2 == null)) return false;
        return date1.compareTo(date2) <= 0;
    }

    public static final boolean beforeNow(Date date1) {
        return before(date1, new Date());
    }

    public static final boolean after(Date date1, Date date2) {
        return !before(date1, date2);
    }

    public static final boolean afterNow(Date date1) {
        return after(date1, new Date());
    }

    /**
     * 判断是否是当月的第一天
     * 
     * @param date
     * @return
     */
    public static boolean isFirstDayOfMonth(Date date) {
        if (date == null) throw new RuntimeException("Date must be not null");
        Calendar cal = getCalendar(date);
        int dayIndex = cal.get(Calendar.DAY_OF_MONTH);
        return (dayIndex == 1);
    }

    /**
     * 得到本月的最后一天
     * 
     * @return
     */
    public static int getMonthLastDay(Date date) {
        Calendar cal = getCalendar(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回上个月的1号的日期
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfLastMonth(Date date) {
        if (date == null) throw new RuntimeException("Date must be not null");
        Calendar cal = getCalendar(date);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    // 返回是哪一年
    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    // 返回一年中的第几天
    public static int getDayOfYear(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    // 返回日历
    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 获取上月第一天
     * 
     * @param date
     * @return
     */
    public static Date getLastFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();// 获取当前日期
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取本月第一天
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();// 获取当前日期
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取下月第一天
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfNextMonth(Date date) {
        Calendar c = Calendar.getInstance();// 获取当前日期
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取上月最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 0);
        return c.getTime();
    }

    /**
     * 获取上月最后一天再过一天
     * 
     * @param date
     * @return
     */
    public static Date getAfterLastLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 当天的凌晨
     * 
     * @param date
     * @return
     */
    public static Date weeHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        // 时分秒（毫秒数）
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        // 凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);
        return cal.getTime();
    }

    public static final String NO_REMAIN_TIME = "00:00:00";

    public static String getTimeByLastDate(int basePayTime, Date d1, Date d2) {
        double d = basePayTime * 3600 - (d1.getTime() - d2.getTime()) / 1000;
        if (d < 0) {
            return NO_REMAIN_TIME;
        }
        int hh = (int) d / 3600;
        int mm = (int) (d - hh * 3600) / 60;
        int ss = (int) d - hh * 3600 - mm * 60;
        return (hh < 10 ? "0" + hh : hh) + ":" + (mm < 10 ? "0" + mm : mm) + ":" + (ss < 10 ? "0" + ss : ss);
    }
    
    /**
     * 存在时区问题，使用时注意
     */
    public static long toDateTime(Object obj, String type) {
        if (obj != null) {
            DateFormat format = new SimpleDateFormat(type);
            try {
                if (obj instanceof Date) {
                    return format.parse(format.format(obj)).getTime();
                } else {
                    return format.parse(TypeUtil.toString(obj)).getTime();
                }
            } catch (ParseException e) {
                throw new RuntimeException("格式转换出错:" + obj, e);
            }
        }
        return 0;
    }
}
