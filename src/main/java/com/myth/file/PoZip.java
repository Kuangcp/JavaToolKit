package com.myth.file;

import com.myth.mysql.Mysql;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 使用的是暴力算法，当位数达到4位时字典就无法承受了，而且排除汉字的字典就如此庞大，
 * 性能必须要改进
 * @author Myth
 * 2017年1月1日 下午7:46:00
 */
public class PoZip {

	private static List<String>Codes;

	public static void main(String[] args) throws Exception {
		//获取字符码
		List<String>Lib;
		Codes = getCode();
		Lib = getCode();

//-------------------------------------
//		int Temp = 3;
//		for(int i=0;i<Temp-1;i++){
//			Lib = LibsPlus(Lib);
//		}
//		disp(Lib);
//		createCodeFile(Lib, "F:\\Tool\\code4.txt");
//		readfile("F:\\Tool\\code4.txt");
//		Rush(Lib);
//---------------------------------
//		CompressUtil.unzip("F:\\Tool\\ui.zip", "F:\\Tool\\ui", "12");

		// 写文件
		plusLibLengthByFile( "F:\\Tool\\code3.txt",  "F:\\Tool\\code4.txt");
	}
	/**读取文件*/
	public static void readfile (String filename){
		File file = new File(filename);
		BufferedReader reader = null;
		try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	/**创建文件*/
	public static void createCodeFile(List<String>file,String path){
		BufferedWriter bw = null;
		OutputStream out = null;
		OutputStreamWriter os = null;
		try {
			out = new FileOutputStream(path);
			os = new OutputStreamWriter(out);
			bw = new BufferedWriter(os);

			for (String aFile : file) {
				bw.write(aFile + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IO有异常");
		}finally {
			try {
				assert bw != null;
				bw.close();
				os.close();
				out.close();
			}catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("资源关闭异常");
			}
		}
		System.out.println("创建文件完成");
	}
	/**追加文件*/
	public static void appendMethodB(String fileName, List<String>lib) {
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			for(int i=0;i<lib.size();i++){
				writer.write(lib.get(i));
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**运行*/
	public static void startSearchPassword(List<String>Lib){
		boolean flag = true;
		int size = Lib.size();
		int index = 0;
		while(flag){
			flag = false;

			try {
				String temp = Lib.get(index);
				index++;
				if(index%51==0) System.out.println("已经尝试"+index);
//				System.out.println(temp);
				CompressUtil.unzip("F:\\Tool\\ui.zip", "F:\\Tool\\ui", temp);
			} catch (ZipException e) {
				flag = true;
				//			e.printStackTrace();
			}
			if(index>=size)flag = false;
		}
		System.out.println("运行完毕");
	}

	/**将传进来的库扩充一个长度*/
	public static List<String> plusLibLength(List<String> lib){
		List<String> result = new ArrayList<String>();

		for (String temp : Codes) {
			for (String aLib : lib) {
				result.add(temp + aLib);
			}
		}
		return result;
	}
	/**将传进来的库扩充一个长度,使用文件操作
	 */
	public static void plusLibLengthByFile(String from,String to) throws Exception{
		File file = new File(from);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		FileWriter writer = new FileWriter(to, true);
		String temp = null;
		//读取文件的一行直到结束
		while ((temp = reader.readLine()) != null) {
//        	temp = temp.substring(0, temp.length()-1);
			for (String Code : Codes) {
				writer.write(temp + Code + "\n");
			}
		}
	}


	/**获取原始字符*/
	public static List<String> getCode(){
		Mysql db = new Mysql("test","3306","root","123456");
		String srcs = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		List<String> list = new ArrayList<String>();
		for(int i=0;i<srcs.length();i++){
			list.add(srcs.charAt(i)+"");
		}
//    	for(int i=0;i<list.size();i++){
//    		System.out.println(list.get(i));
//    	}
		return list;
	}
	public static void disp(List<String>lib){
		for(int i=0;i<lib.size();i++){
			System.out.print(lib.get(i)+"-");
			if(i%11==0) System.out.println();
		}
	}
}