package com.myth.countLine;

import com.myth.time.GetRunTime;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 求某路径下的所有文件的行数
 * @author  Myth
 * @date 2016年10月6日 上午11:13:03
 * @TODO
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
	static Map<String,Long> typeMap  = new HashMap<String,Long>();
	static String [] type = {
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
		for (int i=0;i<type.length;i++){
			typeMap.put(type[i], 0L);
		}
	}
	public static void main(String[] args) {
		GetRunTime s = new GetRunTime();
		s.Start();
		System.out.println("请输入项目根目录，资源管理器复制路径即可");
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		sc.close();
		AllFile(path);
		for(int i=0;i<type.length;i++){
			System.out.println(type[i]+" : "+typeMap.get(type[i]));
		}
		s.End("");
	}
	/**
	 * 
	 * @param path 一个文件夹的PATH
	 * @return 
	 */
	public static void AllFile(String path) {
       File file = new File(path);
       if (!file.exists()) {
    	   System.out.println("文件不存在");
       }
       if (!file.isDirectory()) {
    	   System.out.println("路径不是目录");
       }
       //file是一个文件夹
       String[] tempList = file.list();
       File temp = null;
       //遍历文件夹下所有文件
       for (int i = 0; i < tempList.length; i++) {
    	   //拼接好全路径
          if (path.endsWith(File.separator)) {//如果路径是带有了系统默认的分隔符（即斜杠）就直接和文件名拼接
             temp = new File(path + tempList[i]);
          } else {
              temp = new File(path + File.separator + tempList[i]);
          }
          //由全路径判断，如果是文件就计数，否则进入递归
          if (temp.isFile()) {
//	        	 System.out.println(temp.toString());
        	  String filename = temp.toString();
        	  String [] fis = filename.split("\\.");
        	  String type = fis[fis.length-1];
        	  if(typeMap.containsKey(type)){
        		  long rowNums = getTotalLines(filename);
        		  long totalNums = typeMap.get(type);
//        		  System.out.println(filename+"  :  "+rowNums);
        		  typeMap.remove(type); 
        		  typeMap.put(type, totalNums+rowNums);
        	  }
          }
          if (temp.isDirectory()) {
             AllFile(path + "/" + tempList[i]);//递归进入文件夹
          }
       }
	 }
	/**
	 * 获取某文件的所有行数
	 * @param path
	 * @return
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
