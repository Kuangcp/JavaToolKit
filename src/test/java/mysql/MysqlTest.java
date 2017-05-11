package mysql;

import com.myth.mysql.Mysql;
import org.junit.Test;

/**
 * Created by mythos on 17-5-11
 * By kuangchengping@outlook.com
 */
public class MysqlTest {
    @Test
    public void testRun(){
        Mysql mysql = new Mysql();
        assert mysql.getConnection()!=null;
        mysql = new Mysql("jadmin","3306","root","mysql1104");
        assert mysql.getConnection()!=null;
    }
}