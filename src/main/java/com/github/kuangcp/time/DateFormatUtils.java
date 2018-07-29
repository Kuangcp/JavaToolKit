package com.github.kuangcp.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mythos on 17-5-8
 * 日期格式化工具类
 */
public class DateFormatUtils {

  private static SimpleDateFormat format = new SimpleDateFormat();
  private static String YYYY_MM_DD_HH_MM_SS = "YYYY-MM-dd HH:MM:SS";
  private static String YYYY_MM_DD_HH_MM = "YYYY-MM-dd HH:MM";

  /**
   * YYYY-MM-dd HH:MM:SS
   */
  public static String toYMDHMS(Date date) {
    return format(date, YYYY_MM_DD_HH_MM_SS);
  }

  /**
   * YYYY-MM-dd HH:MM
   */
  public static String toYMDHM(Date date) {
    return format(date, YYYY_MM_DD_HH_MM);
  }

  public static String format(Date date, String pattern) {
    format.applyPattern(pattern);
    return format.format(date);
  }

}
