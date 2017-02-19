package com.myth.file;

import java.io.File;

public class DeleteFile {

	/**
	 * 删除指定文件夹下所有文件，包括文件夹树（递归删除）
	 * @param path 指定路径的文件
	 * @return 是否成功删除
	 */
	public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	    	   System.out.println("文件不存在");
	    	   return flag;
	       }
	       if (!file.isDirectory()) {
	    	   System.out.println("路径不是目录");
	    	  return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             System.out.println("先删除文件夹里面的文件");
	             delFolder(path + "/" + tempList[i]);//再删除空文件夹
	             System.out.println("再删除空文件夹");
	             flag = true;
	          }
	       }
	       return flag;
	     }
	
	/**
	 * 删除指定路径下的某一文件夹
	 * @param folderPath 文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
