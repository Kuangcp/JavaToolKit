package com.myth.reflect;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
/**
 * 读取 properties 文件 的类 
 * @author Myth
 * @date 2016年7月29日
 */
public class Config {

	private Properties cfg = new Properties();
	public Config(){}
	// maven 结构 resources下的a.yml : /a.yml
	public Config(String path){
		try {
			InputStream is = this.getClass().getResourceAsStream(path);
			cfg.load(is);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e); 
		}
	}
	public String getString (String key){

		try {
			return new String(cfg.getProperty(key).getBytes("ISO8859-1"), "utf-8");
		}catch(Exception r){
			System.out.println("配置文件读取的编码设置错误");
		}
		return cfg.getProperty(key);
	}
	public int getInt(String key){
		return Integer.parseInt(cfg.getProperty(key));
	}
	public double getDouble(String key){
		return Double.parseDouble(getString(key));
	}


}
