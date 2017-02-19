package com.myth.mysql;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将指定的SQL语句的结果集生成一个Excel单Sheet文件
 * @author  Myth
 * @date 2016年8月25日 下午11:06:57
 *
 */
public class TableTurnExcel {

	private List<String []> tables;
	private int RowNum;
	private int ColNum;
//	private String SheetName;
//	private String path;
	private HSSFWorkbook wb;
	private HSSFCellStyle titleStyle;
	private HSSFCellStyle contentStyle;
	/**
	 * 
	 * @param sql 需要查询的SQL语句
	 * @param title Excel表格的标题
	 */
	{
		//创建工作薄
		wb = new HSSFWorkbook();
		//创建标题样式
		titleStyle = createTitleStyle(wb);
		//创建正文样式
		contentStyle = createContentStyle(wb);
	}
	/**
	 * 生成单个Sheet 的表格文件
	 * @param sql String 查询的SQL
	 * @param title String 标题
	 * @param sheetName String Sheet名字
	 * @param Path String 生成文件的目的路径
	 */
	public static void Run(String sql,String title,String sheetName,String Path){
		TableTurnExcel te = new TableTurnExcel();
		
		te.WriteExcelSheet(title,sql,sheetName);
		te.CreateFile(te.wb, Path);
		
	}
	/**
	 * 生成多Sheet 的表格文件 
	 * @param sql String[] 查询的SQL
	 * @param title String[] 标题
	 * @param sheetName String[] Sheet名字
	 * @param Path String 生成文件的目的路径
	 */
	public static void Runs(String [] sql,String[] title,String[] sheetName,String Path){
		TableTurnExcel tes = new TableTurnExcel();
		for (int i=0;i<sql.length;i++){
			tes.WriteExcelSheet(title[i], sql[i],sheetName[i]);
		}
		tes.CreateFile(tes.wb, Path);
	}
	/**
	 * 生成多个Sheet 的表格文件
	 * 不加SheetName参数，默认是数字123
	 * @param sql String []
	 * @param title String []
	 * @param Path String []
	 */
	public static void Runs(String [] sql,String[] title,String Path){
		TableTurnExcel tes = new TableTurnExcel();
		for (int i=0;i<sql.length;i++){
			tes.WriteExcelSheet(title[i], sql[i],(i+1)+"");
		}
		tes.CreateFile(tes.wb, Path);
	}
	/**
	 * 指定列名，动态生成多个列，组合为一个表
	 * @param ColName String[] 列名的数组
	 * @param sql String[] 每条列对应一条SQL（一条SQL可以获取多值，但是只把第一个值加入表格中作为一列）
	 * @param title Sheet的标题
	 * @param Path 文件生成路径
	 */
	public static void CreateDynamicTable(String []ColName,String sql[],String title,String Path){
		TableTurnExcel te = new TableTurnExcel();
		te.CreateSheet(ColName, sql, title, Path);
		te.CreateFile(te.wb, Path);
	}
	/**
	 * 将一个List 转换成一个xls文件
	 * @param title
	 * @param data
	 * @param Path
	 */
	public static void ListToExcel (String title,List<String []> data,String Path){
		TableTurnExcel te = new TableTurnExcel();
		te.ListToSheet(title, data, title);
		te.CreateFile(te.wb, Path);
	}
	/**
	 * 将多个List 形式的数据转换成多Sheet
	 * @param title String [] 每个Sheet的标题
	 * @param data List<List<String []>> 表格所有数据的集合
	 * @param Path 路径
	 */
	public static void ListToExcels (String[] title,List<List<String []>> data,String Path){
		TableTurnExcel te = new TableTurnExcel();
		for(int i=0;i<data.size();i++){
			te.ListToSheet(title[i], data.get(i), i+"");
		}
		te.CreateFile(te.wb, Path);
	}
	/**
	 * 创建课表
	 * @param title
	 * @param data
	 * @param Path
	 */
	public static void CreateKB(String title,List<String []> data,String Path){
		
	}
	public static void main(String[] args) {
		//单Sheet
//		TableTurnExcel e = new TableTurnExcel();
//		e.WriteExcelSheet("辅导员","select * from assitant ","1");
//		e.CreateFile(e.wb,"F:/A.xls");
		
		//多Sheet
		/*String [] sql,title,sheetName;
		sql = new String [3];
		title = new String [3];
		sheetName = new String [3];
		sql[0]="select * from assitant";
		sql[1]="select * from major";
		sql[2]="select * from course";
		title[0]="辅导员";
		title[1]="专业";
		title[2]="课程";
		sheetName[0]="1";
		sheetName[1]="2";
		sheetName[2]="3";
		String Path="F:/ASs.xls";
		
		Runs(sql, title, Path);*/
		Mysql db = new Mysql("student","root","ad");
		List<String[]> data = db.SelectReturnList("select * from student");
		ResultSet rs = db.SelectAll("select * from student");
		
		try {
			String [] ti = new String[rs.getMetaData().getColumnCount()];
			for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
//					System.out.println(rs.getMetaData().getColumnName(i));
//				System.out.println(rs.getMetaData().getColumnLabel(i));
//				if(i!=1 && i%2==1){
					String name = rs.getMetaData().getColumnLabel(i);
					ti[i-1] = name;
					
//					System.out.println(name);
//				}
				
			}
			data.add(0,ti);//加入到第一
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ListToExcel("titles", data, "F:/List.xls");
	}
	/**创建一个动态列的表加入到Sheet
	 * 原理是：传入表头数组，然后同样大小的SQL数组，一条SQL得到一个列的值，进行拼接 
	 */
	@SuppressWarnings("deprecation")
	private void CreateSheet(String []ColName,String sql[],String title,String Path){
		HSSFSheet sheet = wb.createSheet(title);
		HSSFRow row ;
		HSSFCell cell ;
		//得到所有数据
		List<List<String []>> list = new ArrayList<List<String []>>();
		Mysql db = new Mysql("student","root","ad");
		for (int i=0;i<sql.length;i++){
			list.add(db.SelectReturnList(sql[i]));
		}
		//注意行和列都是数组下标格式	
		//创建单元格合并的标题 
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString(title));
		cell.setCellStyle(titleStyle);
		sheet.addMergedRegion(new Region(0,(short)0,0,(short)(ColNum-1)));//合并单元格
		//创建表格列头名字
		row = sheet.createRow(1);
		for(int i=0;i<ColName.length;i++){
			cell = row.createCell(i);
			cell.setCellValue(new HSSFRichTextString(ColName[i]));
		}
		//创建正文表格数据
		for (int i=0;i<list.get(0).size();i++){//行数
			row = sheet.createRow(i+2);//除去标题和表头
			for (int j=0;j<list.size();j++){//列数
				cell = row.createCell(j);
				//动态变化列下标，行下标在外层循环变化，只获取查询语句结果集中第一个字段的值
				cell.setCellValue(new HSSFRichTextString(list.get(j).get(i)[0].trim()));
				cell.setCellStyle(contentStyle);
			}
		}
	}
	/**可重复调用以达到在一个工作簿生成多个Sheet的目的*/
	@SuppressWarnings("deprecation")
	private void WriteExcelSheet(String title,String sql,String SheetName){
		WriteDataBySql(sql);
		//创建工作表
		HSSFSheet sheet = wb.createSheet(SheetName);
		HSSFRow row ;
		HSSFCell cell ;
		//创建标题
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString(title));
		cell.setCellStyle(titleStyle);
		sheet.addMergedRegion(new Region(0,(short)0,0,(short)(ColNum-1)));//合并单元格
//		System.out.println(RowNum+":"+ColNum);
		//创建正文
		for (int i=0;i<RowNum;i++){
			row = sheet.createRow(i+1);
			for (int j=0;j<ColNum;j++){
				cell = row.createCell(j);
				cell.setCellValue(new HSSFRichTextString(tables.get(i)[j].trim()));
				cell.setCellStyle(contentStyle);
			}
		}
	}
	/**
	 * 
	 * @param title
	 * @param data
	 * @param SheetName
	 */
	private void ListToSheet(String title,List<String[]> data,String SheetName){
		HSSFSheet sheet = wb.createSheet(SheetName);
		HSSFRow row ;
		HSSFCell cell ;
		int ColNum = data.get(0).length;
		int RowNum = data.size();
		//创建标题
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString(title));
		cell.setCellStyle(titleStyle);
		sheet.addMergedRegion(new Region(0,(short)0,0,(short)(ColNum-1)));//合并单元格
//		System.out.println(RowNum+":"+ColNum);
		//创建正文
		for (int i=0;i<RowNum;i++){
			row = sheet.createRow(i+1);
			for (int j=0;j<ColNum;j++){
				cell = row.createCell(j);
				cell.setCellValue(new HSSFRichTextString(data.get(i)[j].trim()));
				cell.setCellStyle(contentStyle);
			}
		}
	}
	/**
	 * 最终将工作簿（包含单个或多个Sheet）创建为文件
	 * @param wb 工作簿
	 * @param path 生成文件的路径
	 */
	private void CreateFile(HSSFWorkbook wb,String path){
		//将工作簿写入输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{  
            wb.write(os);  
        }catch(IOException e){  
            e.printStackTrace();  
        }  
        byte[] xls = os.toByteArray();  
        //将输出流输出到文件中去
        File file = new File(path);  
        OutputStream out = null;  
        try {  
             out = new FileOutputStream(file);  
             try {  
                out.write(xls);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } catch (FileNotFoundException e1) {  
            e1.printStackTrace();  
        }
        System.out.println("写入Excel成功");
	}
	
	/**把SQL查询结果转换成List集合,载入属性tables中*/
	private void WriteDataBySql(String sql){
		Mysql db = new Mysql("student","root","ad");
		tables = db.SelectReturnList(sql);
		RowNum = tables.size();
		ColNum = tables.get(0).length;
		System.out.println("读取数据库并加载入List集合成功");
	}
	/**创建标题样式*/
	private HSSFCellStyle createTitleStyle(HSSFWorkbook wb){
		HSSFFont titleFont = wb.createFont();  
		titleFont.setFontHeightInPoints((short)14);  
		titleFont.setFontName("楷体");  
		titleFont.setColor(HSSFColor.RED.index);  
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
		
        HSSFCellStyle titleStyle = wb.createCellStyle();  
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        titleStyle.setFont(titleFont);
        return titleStyle;
	}
	/**创建正文样式*/
	private HSSFCellStyle createContentStyle(HSSFWorkbook wb){
		HSSFFont contentFont = wb.createFont();  
        contentFont.setFontHeightInPoints((short)12);  
        contentFont.setFontName("宋体"); 
        contentFont.setColor(HSSFColor.BLACK.index);  
        contentFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
		
        HSSFCellStyle contentStyle = wb.createCellStyle();  
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        contentStyle.setFont(contentFont);
        return contentStyle;
	}
}
