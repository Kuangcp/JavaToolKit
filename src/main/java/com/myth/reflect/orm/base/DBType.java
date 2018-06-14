package com.myth.reflect.orm.base;

/**
 * Created by https://github.com/kuangcp
 * 数据库类型, 并设置好驱动类
 * @author kuangcp
 */
public enum DBType {
  Mysql("mysql", "com.mysql.jdbc.Driver"), PostgreSQL("postgresql", "org.postgresql.Driver");


  DBType(String type, String driver) {
    this.type = type;
    this.driver = driver;
  }

  private String type;
  private String driver;


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }
}
