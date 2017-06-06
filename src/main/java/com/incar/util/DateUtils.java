package com.incar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouyongbo on 2017/6/5.
 * 时间工具
 */
public class DateUtils {

    /**
     * 功能描述：格式化日期
     * @param dateStr 字符型日期：YYYY-MM-DD 格式
     * @return Date 日期
     */
    public static Date parseStrToDate(String dateStr) {
        String format = "yyyy-MM-dd HH:mm:ss";
        if(dateStr != null && dateStr.length() > 0){
            format = format.substring(0,dateStr.length());
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}
