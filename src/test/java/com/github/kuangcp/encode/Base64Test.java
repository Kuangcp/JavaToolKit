package com.github.kuangcp.encode;

import org.junit.Test;

/**
 * @author kuangcp on 18-8-20-上午9:40
 */
public class Base64Test {

  @Test
  public void test(){
    String decodestr = new String(Base64.decode("MTE2LjQ2Mzg0NzU1MDg0".getBytes()));
    System.out.println(decodestr);
  }
}
