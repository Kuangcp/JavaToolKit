package com.github.kuangcp.encode;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Des3EncryptionUtil {

  public static final String CHAR_ENCODING = "UTF-8";

  public static byte[] encode(byte[] key, byte[] data) throws Exception {
    final String Algorithm = "DESede";

    SecretKey deskey = new SecretKeySpec(key, Algorithm);

    Cipher c1 = Cipher.getInstance(Algorithm);
    c1.init(Cipher.ENCRYPT_MODE, deskey);
    return c1.doFinal(data);
  }

  public static byte[] decode(byte[] key, byte[] value) throws Exception {
    final String Algorithm = "DESede";

    SecretKey deskey = new SecretKeySpec(key, Algorithm);

    Cipher c1 = Cipher.getInstance(Algorithm);
    c1.init(Cipher.DECRYPT_MODE, deskey);
    return c1.doFinal(value);
  }

  public static String encode(String key, String data) {
    try {
      byte[] keyByte = key.getBytes(CHAR_ENCODING);
      byte[] dataByte = data.getBytes(CHAR_ENCODING);
      byte[] valueByte = encode(keyByte, dataByte);
      return new String(Base64.encode(valueByte), CHAR_ENCODING);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String decode(String key, String value) {
    try {
      byte[] keyByte = key.getBytes(CHAR_ENCODING);
      byte[] valueByte = Base64.decode(value.getBytes(CHAR_ENCODING));
      byte[] dataByte = decode(keyByte, valueByte);
      return new String(dataByte, CHAR_ENCODING);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String encryptToHex(String key, String data) {
    try {
      byte[] keyByte = key.getBytes(CHAR_ENCODING);
      byte[] dataByte = data.getBytes(CHAR_ENCODING);
      byte[] valueByte = encode(keyByte, dataByte);
      return toHex(valueByte);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String decryptFromHex(String key, String value) {
    try {
      byte[] keyByte = key.getBytes(CHAR_ENCODING);
      byte[] valueByte = fromHex(value);
      byte[] dataByte = decode(keyByte, valueByte);
      return new String(dataByte, CHAR_ENCODING);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String udpEncrypt(String key, String data) {
    try {
      Key k = updGenerateKey(key);
      IvParameterSpec IVSpec = new IvParameterSpec(new byte[8]);
      Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
      c.init(1, k, ((IVSpec)));
      byte output[] = c.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return new String(Base64.encode(output), StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Key updGenerateKey(String key) {
    try {
      DESedeKeySpec KeySpec = new DESedeKeySpec(UdpHexDecode(key));
      SecretKeyFactory KeyFactory = SecretKeyFactory
          .getInstance("DESede");
      return ((KeyFactory.generateSecret(((KeySpec)))));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String udpDecrypt(String key, String data) {
    try {
      byte[] input = Base64.decode(data.getBytes(StandardCharsets.UTF_8));
      Key k = updGenerateKey(key);
      IvParameterSpec IVSpec = new IvParameterSpec(new byte[8]);
      Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
      c.init(2, k, ((IVSpec)));
      byte output[] = c.doFinal(input);
      return new String(output, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static byte[] UdpHexDecode(String s) {
    byte abyte0[] = new byte[s.length() / 2];
    String s1 = s.toLowerCase();
    for (int i = 0; i < s1.length(); i += 2) {
      char c = s1.charAt(i);
      char c1 = s1.charAt(i + 1);
      int j = i / 2;
      if (c < 'a') {
        abyte0[j] = (byte) (c - 48 << 4);
      } else {
        abyte0[j] = (byte) ((c - 97) + 10 << 4);
      }
      if (c1 < 'a') {
        abyte0[j] += (byte) (c1 - 48);
      } else {
        abyte0[j] += (byte) ((c1 - 97) + 10);
      }
    }
    return abyte0;
  }

  public static String toHex(byte input[]) {
    if (input == null) {
      return null;
    }
    StringBuilder output = new StringBuilder(input.length * 2);
    for (byte anInput : input) {
      int current = anInput & 0xff;
      if (current < 16) {
        output.append("0");
      }
      output.append(Integer.toString(current, 16));
    }

    return output.toString();
  }

  public static byte[] fromHex(String input) {
    if (input == null) {
      return null;
    }
    byte output[] = new byte[input.length() / 2];
    for (int i = 0; i < output.length; i++) {
      output[i] = (byte) Integer.parseInt(
          input.substring(i * 2, (i + 1) * 2), 16);
    }

    return output;
  }
}