package com.myth.mysql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-14  上午11:10
 */
public class MysqlTest {

    private  Mysql mysql;
    @Before
    public void init(){
        BaseConfig config = new BaseConfig().initDriver("com.mysql.jdbc.Driver").initDatabase("test")
                .initHost("localhost").initPort(3306)
                .initUsername("myth").initPassword("ad");
        System.out.println(config.toString());
        mysql = new Mysql(config);
    }
    @Test
    public void testGetConnection() {
        Connection result = mysql.getConnection();
        assert result != null;
    }

    @Test(timeout = 2000)
    public void testQueryBySQL() throws SQLException {
        ResultSet result = mysql.queryBySQL("select * from a");
        assert result != null;
        while (result.next()){
            System.out.println(result.getString(1)+"|"+result.getString(2));
        }
    }

    @Test
    public void testQueryReturnList() {
//        List<String> result = mysql.queryReturnList("sql");
//        Assert.assertEquals(Arrays.<String>asList(new String[]{"String"}), result);
    }

    @Test
    public void testExecuteUpdateSQL() {
        boolean result = mysql.executeUpdateSQL("sql");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testBatchInsertWithAffair() {
        boolean result = mysql.batchInsertWithAffair(new String[]{"sqls"});
        Assert.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme