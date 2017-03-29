package com.myth.reflect;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
/**
 * 读取 properties 文件 的类 
 * @author Myth
 * @date 2016年7月29日
 */
public class Config {

	private Properties cfg = new Properties();
	public Config(){}
	//从src开始的全路径
	public Config(String file){
		try {
			File f = new File(file);
			cfg.load(new FileInputStream(f));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e); 
		}
	}
	public String getString (String key){
		return cfg.getProperty(key);
	}
	public int getInt(String key){
		return Integer.parseInt(cfg.getProperty(key));
	}
	public double getDouble(String key){
		return Double.parseDouble(getString(key));
	}
	
	/**
	 * 测试main函数
	 * @param p
	 */
//	public static void main(String [] p){
//		Config con = new Config("mysql.properties");
//		System.out.println(con.getString("Driver"));
//		System.out.println(con.getString("User"));
//		System.out.println(con.getString("Pass"));
//		System.out.println(con.getString("URL"));
//	}
}
