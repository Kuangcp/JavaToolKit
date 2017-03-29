package com.myth.time;

import java.util.Calendar;
/**
 * 获取语句运行时间的类，实例化一次后，可多次使用
 * @author  Myth
 * @date 2016年9月29日 下午7:47:35
 *
 */
public class GetRunTime {
	private long start;
	private long end;

	/**
	 * 开始计时
	 */
	public void Start(){
		start = Calendar.getInstance().getTimeInMillis();
	}
	/**
	 * 输出耗费时间
	 * @param s 起始输出的字符串
	 */
	public void End(String s){
		end = Calendar.getInstance().getTimeInMillis();
		long waste = end-start;
		long ms = waste,sec=0,min=0,hour=0;

		hour = ms/3600000;
		ms-=hour*3600000;
		min = ms/60000;
		ms-=min*60000;
		sec = ms/1000;
		ms-=sec*1000;

		System.out.println("========\n"+s+"\n耗时：<"+waste+"ms>\n格式：<"+hour+"h:"+min+"m:"+sec+"s:"+ms+"ms>");
	}
}
