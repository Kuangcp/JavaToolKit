package com.myth.qrcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 因为二维码具有一定的容错能力，所以可以给二维码进行美化，加上logo……
 * ，但是都存在阅读失败的问题 TODO 找到问题的所在 是因为那张图片读取不到二维码
 *
 * @author Myth
 * 2016年10月4日 上午8:45:41
 */
public class ReadQRCode {

  private static Logger logger = LoggerFactory.getLogger(ReadQRCode.class);

  /**
   * 读取绝对路径下的二维码图片
   *
   * @param path 绝对路径
   * @return 返回二维码文本，失败返回null
   */
  public static String readQRCodeFromFile(String path) {
//    MultiFormatReader formatReader = new MultiFormatReader();
    File file = new File(path);
    BufferedImage image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      logger.error("读取指定路径 " + path + "图片失败 ", e);
    }
    return readQRCode(image);

  }

  /**
   * 阅读URL图片
   *
   * @param url 图片的URL
   * @return 图片上二维码内容 文件读取失败返回null
   */
  public static String readQRCodeFromURL(String url) {
    InputStream file;
    try {
      file = new URL(url).openStream();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    BufferedImage image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return readQRCode(image);
  }

  private static String readQRCode(BufferedImage image) {
    MultiFormatReader formatReader = new MultiFormatReader();
    BinaryBitmap binaryBitmap = new BinaryBitmap(
        new HybridBinarizer(new BufferedImageLuminanceSource(image)));
    HashMap hints = new HashMap<>();//创建属性
    hints.put(EncodeHintType.AZTEC_LAYERS, "utf-8");//设置编码
    Result result = null;
    try {
      result = formatReader.decode(binaryBitmap, hints);
    } catch (NotFoundException e) {
      e.printStackTrace();
    }
    if (result != null) {
      System.out.println("结果 " + result.toString());
      return result.toString();
    }
    return null;
  }
}
