package com.myth.mysql;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Myth on  2016年8月5日 下午8:15:01
 *  从数据库的表里，生成指定路径下类文件，现在基本用不上了，新建实体类，然后使用jpa的update就会自动建表了
 */
public class TableTurnClass{
	private static String FileName="";
	private static String database="",user="",pass="",table="";
	private static String Driver = "com.mysql.jdbc.Driver";
	private static String URL ="";
	private PreparedStatement ps = null;
	private Connection cn = null;
	private ResultSet rs = null;
	private static List<String> codes = null;
	private static List<Table> tableInfo = null;
	private static String packageName;
	private static String filePath;

	/**
	 * 创建某数据库下一个指定表或视图
	 * @param filePaths 真正的路径 .分隔，根目录为src
	 * @param packages 指定包名 .分隔，根目录为src
	 * @param db 数据库
	 * @param user 用户
	 * @param pass 密码
	 * @param ta 指定表
	 * @param console 是否控制台输出
	 */
	public static void createOneTable(String filePaths,String packages,String db,String user,String pass,String ta,boolean console){
		boolean flag = true;
		try{
			filePath = filePaths;
			packageName = packages;
			new TableTurnClass(db,user,pass,ta);//初始化对象
			FileName = GetFileName(table);
			tableInfo = getTableList();
			codes = CreateCode(tableInfo);
			if(console){
				//控制台输出
				Display(tableInfo);
				DisPlayList(codes);
			}
			CreateClass();
		}catch(Exception e ){
			e.printStackTrace();
			System.out.println("创建失败");
			flag = false;
		}
		if(flag)System.out.println("创建成功");
	}

	/**
	 * 创建某数据库下所有表和视图
	 * @param filePaths 真正的路径 .分隔，根目录为src
	 * @param packages 指定包名
	 * @param db 数据库
	 * @param us 用户
	 * @param pa 密码
	 * @param console 是否控制台输出
	 */
	public static void createAllTableFromCurrentDatabase(String filePaths,String packages,String db,String us,String pa,boolean console){
		boolean flag = true;
		try{
			filePath = filePaths;
			packageName = packages;
			TableTurnClass test = new TableTurnClass(db,us,pa);
			List<String>tables = test.getTables();
			assert tables != null;
			for (String table1 : tables) {
				table = table1;//获取表名
				FileName = GetFileName(table);
				tableInfo = getTableList();
				codes = CreateCode(tableInfo);
				if (console) {
					//控制台输出
					Display(tableInfo);
					DisPlayList(codes);
				}
				CreateClass();
			}
		}catch(Exception e ){
			e.printStackTrace();
			System.out.println("创建失败");
			flag = false;
		}
		if(flag)System.out.println("创建成功");
	}

	/**构造器，只是初始化参数值*/
	private TableTurnClass(String db,String id,String pa){
		database = db;
		user = id;
		pass = pa;
		URL ="jdbc:mysql://localhost:3306/"+database+"?user="+user+"&password="+pass
				+"&userUnicode=true&characterEncoding=UTF8";
	}
	private TableTurnClass(String db,String id,String pa,String ta){
		this(db, id, pa);
		table = ta;
	}
	/*
	 * 生成持久类
	 */
	private static void CreateClass(){
		String name = FileName+".java";
		List2File(codes, name);
		System.out.println("转换了 "+database+" 下的 "+FileName+" 持久类成功");
	}

	/**
	 * 将List代码输出到指定package下
	 * @param file 代码集
	 * @param fileName 给定生成的文件的文件名
	 */
	private static void List2File(List<String> file,String fileName){
		BufferedWriter bw = null;
		OutputStream out = null;
		OutputStreamWriter os = null;
		String packages = filePath.replace(".", "/");//置换
		try {
			out = new FileOutputStream("./src/"+packages+"/"+fileName);
			os = new OutputStreamWriter(out);
			bw = new BufferedWriter(os);

			for (String aFile : file) {
				bw.write(aFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IO有异常");
		}finally {
			try {
				if(bw!=null)bw.close();
				if(os!=null)os.close();
				if(out!=null)out.close();
			}catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("资源关闭异常");
			}
		}
	}
	/**
	 * 获取数据库下所有表的元数据信息
	 * @return List<Table对象>
	 */
	private static List<Table> getTableList(){
		TableTurnClass r = new TableTurnClass(database,user,pass,table);
		List<Table> tables = new ArrayList<>();
		ResultSet rs = r.SelectAll("desc "+table);

		try {
			while(rs.next()){
				//System.out.println(rs.getString(1));
				Table tb = new Table(rs.getString(1),rs.getString(2));
				tables.add(tb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			r.closeAll();
		}
//		for(int i=0;i<tables.size();i++){
//			System.out.println(tables.get(i).name+"::::"+tables.get(i).type);
//		}
		return tables;
	}
	/**
	 * 生成代码
	 * @param tableInfo 表的集合，名称和数据类型
	 * @return 当前表对应的持久类所有代码的集合
	 */
	private static List<String> CreateCode(List<Table> tableInfo){
		int LENGTH = tableInfo.size();
		List<String>codesList = new ArrayList<>();
		codesList.add("package "+packageName+";\n\n");
		codesList.add("public class "+FileName+" {\n");
		/* 成员属性*/
		for (Table aTableInfo : tableInfo) {
			String type = aTableInfo.type;
			String name = aTableInfo.name;
			String types = TurnType(type);//类型转换

			aTableInfo.type = types;
			codesList.add("    private " + types + " " + name + ";\n");
		}
		codesList.add("\n");
		/*
			构造器
		 */
		codesList.add("    public "+FileName+"(){}\n");
		StringBuilder Method= new StringBuilder("    public " + FileName + "(");
		boolean flag = true,flag2=true;
		for (Table aTableInfo : tableInfo) {
			flag2 = true;
			Method.append(aTableInfo.type).append(" ").append(aTableInfo.name).append(", ");
			if (Method.length() > 80 && flag) {
				Method.append("\n            ");
				flag = false;
				flag2 = false;
			}
		}
		Method = new StringBuilder(Method.substring(0, Method.length() - 2));
		if(!flag2) Method = new StringBuilder(Method.substring(0, Method.length() - 13));
		Method.append("){");
		for (Table aTableInfo : tableInfo) {
			Method.append("\n        this.").append(aTableInfo.name).append(" = ").append(aTableInfo.name).append(";");
		}
		Method.append("\n    }\n");
		codesList.add(Method.toString());
		codesList.add("\n");
		/*
		 生成SetGet方法
		 */
		for (Table aTableInfo : tableInfo) {
			String types = aTableInfo.type;
			String name = aTableInfo.name;
			//属性名首字母大写
			String Name = aTableInfo.name.substring(0, 1).toUpperCase() + aTableInfo.name.substring(1, aTableInfo.name.length());
			codesList.add("    public " + types + " get" + Name + "(){\n        return " + name + ";\n    }\n");
			codesList.add("    public void set" + Name + "(" + types + " " + name + "){\n       this." + name + " = " + name + ";\n    }\n");
		}
		/*
			toString方法
		 */
		codesList.add("    @Override\n    public String toString(){\n        return \""+FileName+"{\"+\n        \"");
		for(int i=0;i<LENGTH;i++){
			String name = tableInfo.get(i).name;
			if(i!=(LENGTH-1))codesList.add(name+"=\"+"+name+"+\n        \",");
			else codesList.add(name+"=\"+"+name+"+\"}\";\n");
		}
		codesList.add("    }\n");
		codesList.add("}\n");
		return codesList;
	}
	/**将Mysql的数据类型转换成Java数据类型*/
	private static String TurnType(String type){
		if(type.startsWith("int") || type.startsWith("smallint") || type.startsWith("tinyint")){
			type="int";
		}else if(type.startsWith("char") || type.startsWith("varchar") || type.endsWith("text")){
			type="String";
		}else if(type.startsWith("bigint")){
			type="long";
		}else if(type.startsWith("float")){
			type="float";
		}else if(type.startsWith("double")){
			type="double";
		}else if(type.startsWith("decimal")){
			type="java.math.BigDecimal";
		}else if(type.startsWith("date") || type.startsWith("time")){
			type="java.util.Date";
		}

		return type;
	}
	/*
		对数据库表名进行处理
	 */
	private static String GetFileName(String table){
		StringBuilder name = new StringBuilder();
		String [] names = table.split("_");
		//table = table.substring(0,1).toUpperCase()+table.substring(1,table.length());
		if(names.length == 1){
			name = new StringBuilder(table.substring(0, 1).toUpperCase() + table.substring(1, table.length()));
		}else{
			for(String word:names){
				name.append(word.substring(0, 1).toUpperCase()).append(word.substring(1, word.length()));
			}
		}
		return name.toString();
	}
	private ResultSet SelectAll(String sql){
		try {
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
			ps=cn.prepareStatement(sql);

			rs=ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	private void closeAll(){
		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(cn!=null) cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 查询指定数据库下所有表名
	 * @return 表名集合
	 */
	private List<String> getTables(){
		List<String>tables = new ArrayList<>();
		try {
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
			cn.prepareStatement(" use "+database+"").execute();
			ps=cn.prepareStatement( "show tables");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				tables.add(rs.getString(1));
			}
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tables;
	}
	/**控制台输出表的元数据*/
	private static void Display(List<Table> tableMeta){
		for (Table aH : tableMeta) {
			System.out.println(aH.toString());
		}
	}
	/**控制台输出最终代码集*/
	private static void DisPlayList(List<String> lists){
		for (String list : lists) {
			System.out.print(list);
		}
	}
}
	/* 创建Hibernate配置文件
	public static void CreateXml(List<table> Info){
		List<String>files = new ArrayList<String>();
		files.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		files.add("<!DOCTYPE hibernate-mapping PUBLIC\n\t\"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n\t");
		files.add("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\"\n>\n");
		files.add("<hibernate-mapping>\n");
		files.add("\t<class name=\""+packageName+"."+table+"\" table=\""+smalltable+"\" catalog=\""+database+"\">\n");
		if("int".equals(Info.get(0).type)){
			files.add("\t\t<id name=\""+Info.get(0).name+"\" type=\"java.lang.Integer\" >\n");
			files.add("\t\t\t<column name=\""+Info.get(0).name+"\"/>\n");
			files.add("\t\t\t<generator class=\"identity\"></generator>\n");
		}else {
			files.add("\t\t<id name=\""+Info.get(0).name+"\" type=\""+Info.get(0).type+"\" >\n");
			files.add("\t\t\t<column name=\""+Info.get(0).name+"\" length=\"20\" />\n");
			files.add("\t\t\t<generator class=\"assigned\"></generator>\n");
		}
		files.add("\t\t</id>\n");
		for(int i=1;i<Info.size();i++){
			files.add("\t\t<property name=\""+Info.get(i).name+"\" type=\""+Info.get(i).type+"\">\n");
			files.add("\t\t\t<column name=\""+Info.get(i).name+"\" length=\"20\" />\n");
			files.add("\t\t</property>\n");
		}

		files.add("\t</class>\n");
		files.add("</hibernate-mapping>\n");

		DisPlayList(files);
	}*/