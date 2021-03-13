package com.cj.lottery.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *  @Description: 日期工具类
 *  @author: zhao_yd
 *  @Date: 2021/3/7 1:39 下午
 *
 */

@Slf4j
public class DateUtil {

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String C_DATE_TIME_YMD = "yyyyMMdd";

    /**
     * 将日期字符串转换成Date类型
     */
    public static Date format(String dateString, String format) {
        if (ObjectUtils.isEmpty(dateString)) {
            return null;
        }
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(dateString);
        } catch (Exception e) {
            log.error("日期转换失败！" + e);
        }
        return date;
    }

    /**
     * 将date类型转换成指定日期格式的字符串
     */
    public static String stringFormat(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 将String类型日期转换成指定日期格式的字符串
     *
     * @param date:日期字符串
     * @param oldFormat:原日期字符串格式
     * @param newFormat:新日期字符串格式
     * @return
     */
    public static String stringFormatToDate(String date, String oldFormat, String newFormat) {
        Date newDate = DateUtil.format(date, oldFormat);
        return DateUtil.stringFormat(newDate, newFormat);
    }

    /**
     * 日期加减
     *
     * @param date 日期
     * @param day  加的天数若为负则减
     * @return Date
     */
    public static Date addOneDay(Date date, int day) {
        if (date == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 日期加减
     *
     * @param date 日期
     * @param minute  加的分总若为负则减
     * @return Date
     */
    public static Date addMinute(Date date, int minute) {
        if (date == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static String timeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);
        return t;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

    /**
     * 获取据参数时间差几天的日期
     *
     * @return
     */
    public static String getDaysStr(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        Date d = calendar.getTime();
        String result = dateToStr(d, C_DATE_TIME_YMD);
        return result;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换日期格式
     *
     * @param time
     * @return
     */
    public static Date formatTime(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(time);
        } catch (Exception e) {
            log.error("日期转换失败[{}]|异常[{}]", time, e);
        }
        return date;
    }

}
