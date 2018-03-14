package com.myth.mysql;


import com.myth.yml.YamlUtil;

/**
 * Created by https://github.com/kuangcp on 18-3-13  下午9:22
 * Mysql 基础配置
 * @author kuangcp
 */
public class BaseConfig {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String driver = "com.mysql.jdbc.Driver";

    public static BaseConfig initByYaml(){
        BaseConfig config =  YamlUtil.readFile(BaseConfig.class, "src/main/resources/mysql.yml");
        return config==null?new BaseConfig():config;
    }

    public BaseConfig initDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public BaseConfig initHost(String host) {
        this.host = host;
        return this;
    }

    public BaseConfig initPort(int port) {
        this.port = port;
        return this;
    }

    public BaseConfig initDatabase(String database) {
        this.database = database;
        return this;
    }

    public BaseConfig initUsername(String username) {
        this.username = username;
        return this;
    }

    public BaseConfig initPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
