package com.myth.mysql;

import java.util.ArrayList;
import java.util.List;

public class Tables{
	String tablename;//表名
	List <String> name;//字段名称
	List <String> type;//字段类型
	List <String> remark;//字段备注
	List <String> limit;//字段约束
	
	public Tables(){
		remark = new ArrayList<String>();
		name = new ArrayList<String>();
		type = new ArrayList<String>();
		limit = new ArrayList<String>();
	}
	public Tables(String tablename, List<String> name, List<String> type,
			List<String> remark,List<String>limit) {
		super();
		this.tablename = tablename;
		this.name = name;
		this.type = type;
		this.remark = remark;
		this.limit = limit;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public List<String> getName() {
		return name;
	}
	public void setName(List<String> name) {
		this.name = name;
	}
	public List<String> getType() {
		return type;
	}
	public void setType(List<String> type) {
		this.type = type;
	}
	public List<String> getRemark() {
		return remark;
	}
	public void setRemark(List<String> remark) {
		this.remark = remark;
	}
	public List<String> getLimit() {
		return limit;
	}
	public void setLimit(List<String> limit) {
		this.limit = limit;
	}
	@Override
	public String toString() {
		return "Table [tablename=" + tablename + ", name=" + name + ", type="
				+ type + ", remark=" + remark + "]";
	}
	
}

