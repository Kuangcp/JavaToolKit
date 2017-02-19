package com.myth.mysql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 格式：第一行是标题(要保证第一个空格是数据库名)，之后是五行为一个表，第一行是表名，第二行是字段的备注，第三行是字段名，第四行是类型，第五行是简单的字段级约束
 * @author  Myth
 * @date 2016年8月3日 下午11:33:56
 * @TODO 将指定格式的Excel创建成表格
 */
public class ExcelCreateTable {
	private POIFSFileSystem fs;      
    private HSSFWorkbook wb;      
    private HSSFSheet sheet;      
    private HSSFRow row; 
    private static String database;
    private static String user;
    private static String pass;
    private static int sheetNum=0;//最好实现一个机制，自动去获取后面的Sheet（如果有数据的话）
    private static String path="";
    private static List<Tables> alltables;//要注意：这里的Map采用的是自然的行号和列号（1开头）
    private static String[] title ;
    private static boolean showOnConsole;
   
    /**
     * 
     * @param paths 文件的绝对路径，格式：F:/Table.xls
     * @param db 数据库名
     * @param username 用户名
     * @param password 密码
     * @param CurrentSheet 使用的Sheet，默认是0（第一个）
     * @param putOnConsoles 是否在控制台输出相关信息，true就输出
     */
    public static void Run(String paths,String db,String username,String password
    		,int CurrentSheet,boolean putOnConsoles){
    	path = paths;
    	//path = path.replace("\\" , "/");
    	showOnConsole = putOnConsoles;
    	sheetNum = CurrentSheet;
    	database = db;
    	user = username;
    	pass = password;
    	System.out.println("文件路径："+path);
    	ExcelCreateTable ect = new ExcelCreateTable();
    	ect.LoadExcel();
    }
//    //手动配置路径，数据库参数，以及Excel文件合乎规范即可
//	public static void main(String[] args) {
//		/*path="F:/Table.xls";
//		user = "root";
//		pass = "ad";
//		showOnConsole = true;
//		System.out.println(path);
//		ExcelCreateTable ect = new ExcelCreateTable();
//		ect.LoadExcel();*/
//		Run("F:/Table.xls","student","root","ad",0,false);
//	}
//  最终路径E:/Git/Test/Test/Test/excel/
    
//	F:\Table.xls
//	System.out.println("请输入Excel文件的完整路径：");
//	Scanner in = new Scanner(System.in);
//	path = in.nextLine();
//	path = path.replace("\\" , "/");
//	in.close();

	/**
	 * 使用正确的路径，正确加载Excel文件，并且读取到两个集合中，以便创建表格
	 */
	private void LoadExcel(){
		//读取文件流
		InputStream is =null;
    	try { 
	        is = new FileInputStream(path);
	        fs = new POIFSFileSystem(is);  //封装特定的输入流 
            wb = new HSSFWorkbook(fs); //创建工作簿
	    }catch (FileNotFoundException e) {System.out.println("未找到指定路径的文件!"); e.printStackTrace();}
    	catch(IOException ew){System.out.println("创建后两个对象出了问题"); ew.printStackTrace();}
    	catch(Exception es){System.out.println("也不知道什么鬼BUG"); es.printStackTrace(); }
    	
        title = readExcelTitle(is,sheetNum);
        alltables = readExcelContent(is,sheetNum);
        if(showOnConsole)Show();
       //一定要放在最后，因为要等到别的方法把所有的参数给初始化好了，才能正确创建数据库的表格
      CreateTables();
	}

	/**
	 * 获取表格标题 
	 * @param is
	 * @return 返回值是String [] 规定标题只占用Excel一行
	 */
	private String[] readExcelTitle(InputStream is,int sheetNum) {      
		
		sheet = wb.getSheetAt(sheetNum); //第一Sheet     
        row = sheet.getRow(0);//第一行 ,因为指定了第一行是标题，虽然标题可能占据多行，但是第一行必须是标题行，不然也会有错误     
        //标题总列数      
        int colNum = row.getPhysicalNumberOfCells();   //由这里限制了列的大小？
        //int colNum = row.getLastCellNum();
        		System.out.println("标题的列数为："+colNum);
        String[] title = new String[colNum];  
        //遍历标题
        for (int i=0; i<colNum; i++) {      
            title[i] = getStringCellValue(row.getCell(i)); //为什么要将i转型为short     
        }   
        //database = title[0];//得到数据库
        return title;      
    }      
          
	/**
	 * 读取Excel数据内容    
	 * @param is
	 * @return
	 */
	private List<Tables> readExcelContent(InputStream is,int sheetNum) { 
		List <Tables> tables = new ArrayList<Tables>();
		int tableRows = 5;//Excel中的表，行数
		      
        sheet = wb.getSheetAt(sheetNum);      
        //得到总行数      
        int rowNum = sheet.getLastRowNum();
        	System.out.println("正文的列数为:"+rowNum);//说明是除去了第一行的标题来计数的
        //读取正文内容，得到表格数据
    	for(int i=1;i<=rowNum;i+=tableRows){//五行一起
    		//1表名，2备注，3列名，4列类型，5列约束
    		row = sheet.getRow(i);//第一行，为了获取表名
    		Tables T = new Tables();
    		T.setTablename(getStringCellValue(row.getCell(0)).trim());
    		//System.out.println("tablename:"+T.getTablename());
    		
    		HSSFRow remarkRow = sheet.getRow(i+1);//到了第二行备注
    		HSSFRow nameRow = sheet.getRow(i+2);//第三行名字
   		 	HSSFRow typeRow = sheet.getRow(i+3);//第四行类型
   		 	HSSFRow limitRow = sheet.getRow(i+4);//第五行约束
    		int colNum = remarkRow.getPhysicalNumberOfCells();//得到实际列数
    		
        	for(int k=0;k<colNum;k++){//一列一列处理
        		 
        		String name = getStringCellValue(nameRow.getCell(k)).trim();
        		String type = getStringCellValue(typeRow.getCell(k)).trim();
    			String remark = getStringCellValue(remarkRow.getCell(k)).trim();
    			String limit = getStringCellValue(limitRow.getCell(k)).trim();
    			
    			T.remark.add(remark);
    			T.name.add(name);
    			T.type.add(type);
    			T.limit.add(limit);
        	}
        	tables.add(T);
        }
		return tables;
	}
   
    /**    
     * 获取单元格数据内容为字符串类型或者日期类型的数据    
     * @param cell ：Excel单元格    
     * @return String 单元格数据内容    
     */     
    @SuppressWarnings("unused")
	private String getStringCellValue(HSSFCell cell) {      
        String strCell = "";      
        switch (cell.getCellType()) { 
        case Cell.CELL_TYPE_FORMULA:
        	try{
        		/**
        		 * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(cell)判断过程中cell.getNumericCellValue();方法
        		 * 会抛出java.lang.NumberFormatException异常
        		 */
        		if (HSSFDateUtil.isCellDateFormatted(cell)) {//判断是日期类型
            		Date date = cell.getDateCellValue();//从单元格获取日期数据
            		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");//设定转换的格式
            		strCell = formater.format(date);//将日期数据（Date 或者直接输入的格式正确的字符串）转换成String类型
            		break;
            	}else{
            		double Num = cell.getNumericCellValue();   //普通数字类型
    	        	DecimalFormat formatCell = (DecimalFormat)NumberFormat.getPercentInstance();
    	        	formatCell.applyPattern("0");
    	        	strCell = formatCell.format(Num);
    	        	if(Double.parseDouble(strCell)!=Num){
    	        		formatCell.applyPattern(Double.toString(Num));
    	        		strCell = formatCell.format(Num);
    	        	}
    	        	if(".0".equals(strCell.subSequence(strCell.length()-2, strCell.length())))strCell = Integer.parseInt(strCell.substring(0,strCell.length()-2))+"";
    	        		System.out.println("数字："+strCell);
            	}
        	}catch(IllegalStateException e){
        		strCell = String.valueOf(cell.getRichStringCellValue());
        		//strCell =new XSSFCell().getCtCell().getV(); 没有这个JAR包
        	}
        	break;
        case HSSFCell.CELL_TYPE_STRING:      
            strCell = cell.getStringCellValue();      
            break; 
        case HSSFCell.CELL_TYPE_NUMERIC: 
        	if (HSSFDateUtil.isCellDateFormatted(cell)) {//判断是日期类型
        		Date date = cell.getDateCellValue();//从单元格获取日期数据
        		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");//设定转换的格式
        		strCell = formater.format(date);//将日期数据（Date 或者直接输入的格式正确的字符串）转换成String类型
        	}else{
        		double Num = cell.getNumericCellValue();   //普通数字类型
	        	DecimalFormat formatCell = (DecimalFormat)NumberFormat.getPercentInstance();
	        	formatCell.applyPattern("0");
	        	strCell = formatCell.format(Num);
	        	if(Double.parseDouble(strCell)!=Num){
	        		formatCell.applyPattern(Double.toString(Num));
	        		strCell = formatCell.format(Num);
	        	}
	        	if(".0".equals(strCell.subSequence(strCell.length()-2, strCell.length())))strCell = Integer.parseInt(strCell.substring(0,strCell.length()-2))+"";
	        		System.out.println("数字："+strCell);
        	}
            break;      
        case HSSFCell.CELL_TYPE_BOOLEAN:      
            strCell = String.valueOf(cell.getBooleanCellValue());      
            break;      
        case HSSFCell.CELL_TYPE_BLANK:      
            strCell = "";  
            //System.out.print("空格");
            break;      
        default:      
            strCell = "";      
            break;      
        }      
        if (strCell.equals("") || strCell == null) {      
            return "";      
        }      
        if (cell == null) {      
            return "";      
        }      
        return strCell;      
    }
    
	private void CreateTables(){
		PreparedStatement ps = null;
		Connection cn = null;
		ResultSet rs = null;
		
		String Driver = "com.mysql.jdbc.Driver";
		String URL="jdbc:mysql://localhost:3306/"+database+"?user="+user+"&password="+pass+"&userUnicode=true&characterEncoding=UTF8";
		/**用上了事务，因为保证其表格的统一性和原子性*/
		try{
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
			cn.setAutoCommit(false);//取消自动提交
			
			String sql;
			for (int i=0;i<alltables.size();i++){
				Tables t = alltables.get(i);
				sql = "create table "+t.getTablename()+"(";
				for(int j=0;j<t.getName().size();j++){
					sql+=t.getName().get(j)+" "+t.getType().get(j)+" "+t.getLimit().get(j)+" comment '"+t.getRemark().get(j)+"',";
				}
				sql = sql.substring(0,sql.length()-1);
				sql+=")";
				//ps = cn.prepareStatement(sql);
				//ps.execute();
				System.out.println("无异常，说明成功创建了一张表");
				if(showOnConsole)System.out.println("执行的SQL:"+sql);
			}
			
			cn.commit();//无异常再提交
		}catch(Exception e){
			try {
				cn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("增删改查失败");
		}finally{
			try{
				cn.setAutoCommit(true);//改回自动提交
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				if(cn!=null) cn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private void Show(){
		System.out.println("数据库："+database);
    	//控制台输出标题
        System.out.println("获得Excel表格的标题:");      
        for (String s1 : title) {      
            System.out.print(s1 + " ");      
        }      
        System.out.println();
        //控制台输出内容
        System.out.println("获得Excel表格的内容:"); 
        for(int i=0;i<alltables.size();i++){
        	System.out.println(alltables.get(i).toString());
        }
	}
}