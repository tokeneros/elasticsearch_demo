package com.eros.example.elasticsearch.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author: eros
 * @Description:
 * @Date: Created in 2020/3/17 11:00
 * @Version: 1.0
 * @Modified By:
 */
public class DateUtils {

    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 当前日历，这里用中国时间表示
     *
     * @return 以当地时区表示的系统当前日历
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String now() {
        return datetimeFormat.format(getCalendar().getTime());
    }

}
