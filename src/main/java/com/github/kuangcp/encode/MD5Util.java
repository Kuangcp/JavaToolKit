package com.github.kuangcp.encode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class MD5Util {

  public static String encrypt(String dataStr) {
    try {
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.update(dataStr.getBytes(StandardCharsets.UTF_8));
      byte s[] = m.digest();
      StringBuilder result = new StringBuilder();
      for (byte value : s) {
        result.append(Integer.toHexString((0x000000FF & value) | 0xFFFFFF00)
            .substring(6));
      }
      return result.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }

}
