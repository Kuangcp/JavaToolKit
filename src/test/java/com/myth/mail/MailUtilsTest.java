package com.myth.mail;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class MailUtilsTest {

  @Test
  public void testSendBy126() throws Exception {
    MailUtils.sendBy126("receiver");
  }

  @Test
  public void testSendBy163() throws Exception {
    MailUtils.sendBy163("receiver");
  }

  @Test
  public void testSendByQQ() throws Exception {
    MailUtils.sendByQQ("1161502665@qq.com");
  }

  @Test
  public void testSendByOutLook() throws Exception {
    MailUtils.sendByOutLook("receiver");
  }
}