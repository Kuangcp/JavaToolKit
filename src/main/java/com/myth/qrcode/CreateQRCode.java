package com.myth.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * 创建QRcode规范的二维码图片文件
 * @author  Myth
 * @date 2016年10月2日 下午3:01:15
 * @TODO
 */
public class CreateQRCode {

	public static void main(String[] args) {
		CreateQRCode.CreateImage("D:/img1.png",  200, 200, "https://github.com/Kuangcp");
		System.out.println("OK");
	}
	/**
	 * 
	 * @param path 完整的路径包括文件名
	 * @param width 图片宽
	 * @param height 图片高
	 * @param contents 内容
	 */
	@SuppressWarnings("unchecked")
	public static void CreateImage(String path,int width,int height,String contents){
		String format = "png";
		//定义二维码的参数
		@SuppressWarnings("rawtypes")
		HashMap hints = new HashMap();
		hints.put(EncodeHintType.AZTEC_LAYERS, "utf-8");//设置编码
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//设置容错等级： L M Q H
		hints.put(EncodeHintType.MARGIN,1);//设置边距
		
		//生成文件
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height,hints);
			Path file = new File(path).toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, format, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}












