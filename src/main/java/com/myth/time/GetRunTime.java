package com.myth.time;

import java.util.Calendar;
/**
 * 获取语句运行时间的类，实例化一次后，可多次使用
 * @author  Myth on 2016年9月29日 下午7:47:35
 *
 */
public class GetRunTime {
	private long start;

	/**
	 * 开始计时
	 */
	public void startCount(){
		start = Calendar.getInstance().getTimeInMillis();
	}
	/**
	 * 输出耗费时间
	 * @param s 要输出的提示字符串
	 */
	public void endCount(String s){
		long end = Calendar.getInstance().getTimeInMillis();
		long waste = end -start;
		long ms = waste,sec,min,hour;

		hour = ms/3600000;
		ms-=hour*3600000;
		min = ms/60000;
		ms-=min*60000;
		sec = ms/1000;
		ms-=sec*1000;

		System.out.println(">>>>>>  "+s+"\n耗时：<"+waste+"ms>\n格式：<"+hour+"h:"+min+"m:"+sec+"s:"+ms+"ms>  <<<<<<");
	}
}
