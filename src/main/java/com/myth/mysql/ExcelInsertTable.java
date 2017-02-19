package com.myth.mysql;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExcelInsertTable {
	private POIFSFileSystem fs;      
    private HSSFWorkbook wb;      
    private HSSFSheet sheet;      
    private HSSFRow row; 
    private InputStream is =null;
    private int AllRows;//内容的行数
    private int AllCols;//内容的列数
    private int startRow=2;//内容起始行 除去表的列名
    private int sheetNum;//最好实现一个机制，自动去获取后面的Sheet（如果有数据的话）
    private int currentSheet=0;//使用的Sheet,数组下标表示法
    private PreparedStatement ps = null;
	private Connection cn = null;
	private ResultSet rs = null;
	private String Driver = "com.mysql.jdbc.Driver";
	private String URL;
	private Map<Position,String> map;//要注意：这里的Map采用的是自然的行号和列号（1开头）
    private String[] title ;
    
    //private String path;
    /**
     * 规范：默认处理第一个Sheet，标题一行，列名一行
     * @param path 文件完整路径 例如：E:/TsetExcel/Student.xls
     * @param db 数据库
     * @param user
     * @param pass
     * @param table 表
     */
    public static void Run(String path,String db,String user,String pass,String table){
    	ExcelInsertTable eit = new ExcelInsertTable(db, user, pass);
    	eit.InsertTable(path, table);
    }
    public ExcelInsertTable(String db,String user,String pass){
    	URL="jdbc:mysql://localhost:3306/"+db+"?user="+user+"&password="+pass+"&userUnicode=true&characterEncoding=UTF8";
    }
//    测试成功
//    public static void main(String []a){
//    	ExcelInsertTable.Run("E:/TsetExcel/Student.xls", "student", "root", "ad", "student");
//    }
	/** 
	 * @To 这是一对一的通用方法
	 * @DataSource 其标题和内容数据来源就是那个String[] 和 Map，一定要先获取了
	 * @addition 插入到数据库的表中，并且是使用了事务处理
	 * @param path 文件完整路径
	 * @param table 表名
	 */
	private void InsertTable(String path,String table){
		ReadFile(path);
		/**用上了事务，因为保证其表格的统一性和原子性*/
		try{
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
			cn.setAutoCommit(false);//取消自动提交
			String sql;
						
			//插入表的内容 必须保证，Excel的列和数据库的列一一对应
			for (int i=startRow;i<=AllRows;i++){
				sql ="insert into "+table+" values(";
				for (int k=1;k<=AllCols;k++) sql += "'"+map.get(new Position(i,k))+"',";
				sql = sql.substring(0,sql.length()-1);//去掉逗号
				sql+=")";
				System.out.println(i+" 一对一 : "+sql);
				ps=cn.prepareStatement(sql);
				ps.executeUpdate();
				System.out.println(i+": 插入记录成功");
			}
			cn.commit();//无异常再提交
		}catch(Exception e){
			try {
				cn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("增删改查失败");
		}finally {
			try{
				cn.setAutoCommit(true);//改回来
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				if(cn!=null) cn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 输入文件名，拼接得到完整路径
	 * 读取文件打开流，并获取到文件数据内容
	 * @param path 文件的完整路径
	 */
	private void ReadFile(String path){
		//path = "E:/TsetExcel/";
		//System.out.println(path);
    	try { 
	        is = new FileInputStream(path);
	        fs = new POIFSFileSystem(is);  //封装特定的输入流 
            wb = new HSSFWorkbook(fs); //创建工作簿
	    }catch (FileNotFoundException e) {System.out.println("未找到指定路径的文件!"); e.printStackTrace();}
    		catch(IOException ew){System.out.println("创建后两个对象出了问题"); ew.printStackTrace();}
    			catch(Exception es){System.out.println("也不知道什么鬼BUG"); es.printStackTrace(); }
    	
		//将数据读到集合内
		title = readExcelTitle(is,currentSheet);
        map = readExcelContent(is,currentSheet);
        //关闭文件流
        try {
    		if(is!=null) is.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * 获取指定Sheet的表格标题 
	 * @param is,sheetNum
	 * @return 返回值是String [] 就直接设定标题只有一行（）
	 */
	private String[] readExcelTitle(InputStream is,int sheetNum) {      
		sheet = wb.getSheetAt(sheetNum); //第一Sheet 
		
        row = sheet.getRow(0);//第一行 ,因为指定了第一行是标题，虽然标题可能占据多行，但是第一行必须是标题行，不然也会有错误     
        //标题总列数 
        System.out.println(row);
        int colNum = row.getPhysicalNumberOfCells();   //由这里限制了列的大小？
        //int colNum = row.getLastCellNum();
        		System.out.println("标题的列数为："+colNum);
        String[] title = new String[colNum];  
        //遍历标题
        for (int i=0; i<colNum; i++) {      
            title[i] = getStringCellValue(row.getCell(i)); //为什么要将i转型为short     
        }      
        return title;      
    }      
          
	/**
	 * 读取指定Sheet的表格数据内容    
	 * @param is,sheetNum
	 * @return Map<Position,String>
	 */
	private Map<Position,String> readExcelContent(InputStream is,int sheetNum) { 
		Map<Position,String> content = new HashMap<Position,String>();
////////////////////////////////
        sheet = wb.getSheetAt(sheetNum);      
        //得到总行数      
        int rowNum = sheet.getLastRowNum();
		AllRows = rowNum;
        row = sheet.getRow(rowNum-1);  //暂时使用这个方法，全表行数的倒数第二行，我就不信你整个表就只有标题
        int colNum = row.getPhysicalNumberOfCells(); //得到实际列数？     还只是第一行也就是标题
        AllCols = colNum;
        System.out.println("正文得到的列数："+colNum);
/////////////////////////////////////		
		for (int i = 1; i <= rowNum; i++) {   //遍历所有行，除了第一行  数组规则
            row = sheet.getRow(i);      
            for(int j=0;j<colNum;j++) {      
            	//每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据      
            	//也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean   
            	String cellss = getStringCellValue(row.getCell(j)).trim();
            	content.put(new Position(i,j+1), cellss); //这里创建的对象，地址是已经固定了，也不可得到,重写HashCode方法即可让值和地址绑定
                //System.out.println("=="+cellss+"==");
            }      
        }
		return content;
	}
   
    /**    
     * 获取单元格数据内容为字符串，数值，时间类型的数据    
     * @param cell Excel单元格    
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
            		//strCell = String.valueOf(cell.getNumericCellValue());   //普通数字类型
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
	        	//System.out.println(strCell+strCell.length());
	        	if(strCell.length()>2 && ".0".equals(strCell.subSequence(strCell.length()-2, strCell.length())))strCell = Integer.parseInt(strCell.substring(0,strCell.length()-2))+"";
	        		//System.out.println("数字："+strCell);
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
    
    /**将获取到的数据在控制台输出*/
    @SuppressWarnings("unused")
	private void showExcel(){
    	//控制台输出标题
        System.out.println("获得Excel表格的标题:");      
        for (String s1 : title) {      
            System.out.print(s1 + " ");      
        }      
        System.out.println();
    	//控制台输出内容
        System.out.println("获得Excel表格的内容:"); 
        for (int i=1; i<=AllRows; i++) {      
        	for (int k=1;k<=AllCols;k++)
        		System.out.print(new Position(i,k).toString()+""+map.get(new Position(i,k))+"-"); //这里的get就没有办法获取到值，因为创建的对象是一个新的地址  
        	System.out.println();
	    }
    }
    
    /**计算得出有效的Sheet*/
    @SuppressWarnings("unused")
	private void showSheet(){
    	//若有多个Sheet，这里只处理单Sheet
    	sheetNum = wb.getNumberOfSheets();
    	System.out.println("Sheet数量："+sheetNum);
    	int tempSheet=0;
    	for(int i=0;i<sheetNum;i++){
    		HSSFSheet sheet = wb.getSheetAt(i);
    	    int EffectiveRow=0;//有效行数
    	    for (Row row : sheet){
    	    	if(row==null) continue;
    	    	EffectiveRow++;
    	    }
    	    System.out.println("有效的行数："+EffectiveRow);
    	    if(EffectiveRow==0){
    	    	System.out.println("空Sheet");
    	    	continue;
    	    }
    	    tempSheet++;
    	}
    	sheetNum = tempSheet;
    	System.out.println("实际有效的Sheet："+sheetNum);

    }
    
}

