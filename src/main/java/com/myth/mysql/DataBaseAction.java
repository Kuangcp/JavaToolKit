package com.myth.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by mythos on 17-5-11
 * By kuangchengping@outlook.com
 * 本意是实现多种数据库的JDBC工具类
 */
public interface DataBaseAction {
    Connection getConnection();
    List<String []> queryReturnList(String sql);
    boolean batchInsertWithAffair(String [] sqls);
    boolean executeUpdateSQL(String sql);
    ResultSet queryBySQL(String sql);
}