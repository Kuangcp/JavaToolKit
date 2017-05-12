package com.myth.countfileline;

import com.myth.time.GetRunTime;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 求某路径下的所有文件的行数
 * @author  Myth By 2016年10月6日 上午11:13:03
 * F:\JavaWeb\EE\struts-2.5\
java : 364940128
jsp : 2279936
html : 247025212
bat : 0
sql : 0
txt : 5294572
md : 0
xml : 3519932
py : 0
语句运行的耗时 :1262974 ms
 */
public class CountLine {
	private static Map<String,Long> typeMap  = new HashMap<>();
	private static String [] type = {
			"java",
			"jsp",
			"html",
			"bat",
			"sql",
			"txt",
			"md",
			"xml",
			"py"
	};
	static{
		for (String aType : type) {
			typeMap.put(aType, 0L);
		}
	}
	public static void main(String[] args) {
		GetRunTime s = new GetRunTime();
		s.Start();
		System.out.println("请输入项目根目录，资源管理器复制路径即可");
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		sc.close();
		allFile(path);
		for (String aType : type) {
			System.out.println(aType + " : " + typeMap.get(aType));
		}
		s.End("");
	}
	/**
	 * @param path 一个文件夹的PATH,递归该文件夹
	 */
	private static void allFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("文件不存在");
		}
		if (!file.isDirectory()) {
			System.out.println("路径不是目录");
		}
		//file是一个文件夹
		String[] tempList = file.list();
		File temp;
		//遍历文件夹下所有文件
		for (String aTempList : tempList != null ? tempList : new String[0]) {
			//拼接好全路径
			if (path.endsWith(File.separator)) {//如果路径是带有了系统默认的分隔符（即斜杠）就直接和文件名拼接
				temp = new File(path + aTempList);
			} else {
				temp = new File(path + File.separator + aTempList);
			}
			//由全路径判断，如果是文件就计数，否则进入递归
			if (temp.isFile()) {
//	        	 System.out.println(temp.toString());
				String filename = temp.toString();
				String[] fis = filename.split("\\.");
				String type = fis[fis.length - 1];
				if (typeMap.containsKey(type)) {
					long rowNums = getTotalLines(filename);
					long totalNums = typeMap.get(type);
//        		  System.out.println(filename+"  :  "+rowNums);
					typeMap.remove(type);
					typeMap.put(type, totalNums + rowNums);
				}
			}
			if (temp.isDirectory()) {
				allFile(path + "/" + aTempList);//递归进入文件夹
			}
		}
	}
	/**
	 * 获取某文件的所有行数
	 * @param path 得到路径
	 */
	private static int getTotalLines(String path){
		FileReader in;
		int totalLines = 0;
		try {
			in = new FileReader(path);
			LineNumberReader reader = new LineNumberReader(in);
			String strLine = reader.readLine();

			while (strLine != null) {
				totalLines++;
				strLine = reader.readLine();
			}
			reader.close();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalLines;
	}
}