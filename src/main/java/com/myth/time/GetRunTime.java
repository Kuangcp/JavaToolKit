package com.myth.time;

import java.util.Calendar;

/**
 * 获取语句运行时间的类，实例化一次后，可多次使用
 * 使用枚举类实现单例模式, 简单安全 2018-06-14 16:54:12
 *
 * @author Myth on 2016年9月29日 下午7:47:35
 */
public enum GetRunTime {

  INSTANCE;
  private long startRecord;

  public void startCount() {
    startRecord = Calendar.getInstance().getTimeInMillis();
  }

  /**
   * 输出耗费时间
   *
   * @param info 要输出的提示字符串
   */
  public void endCount(String info) {
    long end = Calendar.getInstance().getTimeInMillis();
    long waste = end - startRecord;
    long ms = waste, sec, min, hour;

    hour = ms / 3600000;
    ms -= hour * 3600000;
    min = ms / 60000;
    ms -= min * 60000;
    sec = ms / 1000;
    ms -= sec * 1000;

    System.out.println("---------------------------------\nInfo   : " + info + "\n"
        + "Total  : " + waste + "ms\n"
        + "Format : " + hour + "h : " + min + "m : " + sec + "s : " + ms + "ms");
  }
}
