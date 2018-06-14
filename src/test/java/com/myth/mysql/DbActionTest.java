package com.myth.mysql;

import com.myth.reflect.orm.DBAction;
import com.myth.reflect.orm.DBConfig;
import com.myth.reflect.orm.base.DBType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Ignore
public class DbActionTest {

  private DBAction DBAction;

  @Before
  public void init() {
    DBConfig dbConfig = new DBConfig().setDatabase("test")
        .setDriver("com.DBAction.jdbc.Driver")
        .setHost("localhost")
        .setPort(3306)
        .setUsername("root")
        .setPassword("jiushi");
    DBAction = new DBAction(dbConfig, DBType.Mysql);
  }

  public void initPg() {
    DBConfig dbConfig = new DBConfig().setDatabase("graduate").setDriver("org.postgresql.Driver")
        .setHost("localhost").setPort(5432).setUsername("postgres").setPassword("jiushi");
    DBAction = new DBAction(dbConfig, DBType.PostgreSQL);
  }

  @Test
  public void testGetConnection() {
    Connection result = DBAction.getConnection();
    assert result != null;
  }

  @Test(timeout = 2000)
  public void testQueryBySQL() throws SQLException {
    ResultSet result = DBAction.queryBySQL("select * from a");
    assert result != null;
    while (result.next()) {
      System.out.println(result.getString(1) + "|" + result.getString(2));
    }
  }

  @Test
  public void testQueryReturnList() {
//        List<String> result = DBAction.queryReturnList("sql");
//        Assert.assertEquals(Arrays.<String>asList(new String[]{"String"}), result);
  }

  @Test
  public void testExecuteUpdateSQL() throws SQLException {
    boolean result = DBAction.executeUpdateSQL("sql");
    Assert.assertEquals(true, result);
  }

  @Test
  public void testBatchInsertWithAffair() {
    boolean result = DBAction.batchInsertWithAffair(new String[]{"sqls"});
    Assert.assertEquals(true, result);
  }
}