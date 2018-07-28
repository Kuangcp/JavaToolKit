package com.github.kuangcp.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mythos on 17-5-8
 * 日期格式化工具类
 */
public class DateFormatUtils {

  private static SimpleDateFormat format = new SimpleDateFormat();

  /**
   * YYYY-MM-dd HH:MM:SS
   */
  public static String toYMDHMS(Date date) {
    format.applyPattern("YYYY-MM-dd HH:MM:SS");
    return format.format(date);
  }

  /**
   * YYYY-MM-dd HH:MM
   */
  public static String toYMDHM(Date date) {
    format.applyPattern("YYYY-MM-dd HH:MM");
    return format.format(date);
  }

}
