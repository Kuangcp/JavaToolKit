package com.myth.time;

import java.time.Instant;

/**
 * 获取语句运行时间的类，实例化一次后，可多次使用 使用枚举类实现单例模式, 简单安全 2018-06-14 16:54:12
 *
 * @author Myth on 2016年9月29日 下午7:47:35
 */
public enum GetRunTime {

  INSTANCE;
  private long startRecord;

  public void startCount() {
    startRecord = Instant.now().toEpochMilli();
  }

  /**
   * 输出耗费时间
   *
   * @param info 要输出的提示字符串
   */
  public void endCount(String info) {
    long end = Instant.now().toEpochMilli();
    long totalMillis = end - startRecord;
    long ms = totalMillis, sec, min, hour;

    hour = ms / 3600_000;
    ms -= hour * 3600_000;
    min = ms / 60_000;
    ms -= min * 60_000;
    sec = ms / 1_000;
    ms -= sec * 1_000;

    String format = "▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁\n▌   Info: %s\n"
        + "▌  Total: %3s ms\n▌ Format: %2s h : %2s m : %2s s : %3s ms\n";
    System.out.println(String.format(format, info, totalMillis, hour, min, sec, ms));
  }
}
