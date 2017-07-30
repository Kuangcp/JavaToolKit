package com.myth.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 因为二维码具有一定的容错能力，所以可以给二维码进行美化，加上logo……
 * @author  Myth
 * 2016年10月4日 上午8:45:41
 */
public class ReadQRCode {

	public static void main(String[] args) {
//		"/home/kcp/Code/wechat/itchat4j/QR.jpg"
		ReadQRCode.readQRCodeFromFile(args[0]);
	}
	@SuppressWarnings("unchecked")
	public static void readQRCodeFromFile(String path){
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			File file = new File(path);
			BufferedImage image = ImageIO.read(file);
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			@SuppressWarnings("rawtypes")
			HashMap hints = new HashMap();//创建属性
			hints.put(EncodeHintType.AZTEC_LAYERS, "utf-8");//设置编码
			Result result = formatReader.decode(binaryBitmap,hints);
			System.out.println(result.toString());
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
