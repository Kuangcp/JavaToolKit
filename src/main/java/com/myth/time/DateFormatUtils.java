package com.myth.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mythos on 17-5-8
 * By kuangchengping@outlook.com
 * 日期格式化工具类
 */
public class DateFormatUtils {

  public static String toYMDHMS(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:MM:SS");
    return format.format(date);
  }

  public static String toYMDHM(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:MM");
    return format.format(date);
  }

}
