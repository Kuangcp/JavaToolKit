package mysql;

import com.myth.mysql.Mysql;
import org.junit.Test;

/**
 * Created by Myth on 2017/2/11
 */
public class TestMysql {
    @Test
    public void TestRunnable(){
        Mysql db = new Mysql("test","root","123456");
        String sql = "insert into excel(a,b) values ('3','q');";//insert into inserts values (3,'w');";
        boolean flag = db.updSQL(sql);
        System.out.println("是否成功："+flag);

    }
}
