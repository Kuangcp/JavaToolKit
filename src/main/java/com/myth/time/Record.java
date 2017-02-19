package com.myth.time;

import com.myth.mysql.Mysql;

import java.util.List;
/**
 * 记录开关机时间，但是导出会有一堆问题
 * Created by Myth on 2017/1/3 - 15:08
 */
public class Record {
    public static void main(String[]s){
        Mysql db = new Mysql("bug","root","123456");
        String sql ;
        sql = "select id from shut_start_date where shut_time is null";
        List<String[]> ls = db.SelectReturnList(sql);

        if(ls.size()<1) {
            sql = "insert into shut_start_date(start_time) values(now())";
        }else{
            sql = "update shut_start_date set shut_time = now() where shut_time is null";
        }
        boolean result = db.updSQL(sql);
        if(result){
            System.out.println("Success");
        }else{
            System.out.println("Fail");
        }
    }
}
