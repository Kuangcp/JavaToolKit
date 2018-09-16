package com.github.kuangcp.encode;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-20-上午9:40
 */
@Ignore
public class Base64Test {

  @Test
  public void test(){
    String result = new String(Base64.decode("MTE2LjQ2Mzg0NzU1MDg0".getBytes()));
    System.out.println(result);
  }
}
