package com.myth.reflect.orm;

import com.myth.reflect.orm.base.DBType;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-4-21  下午7:38
 */
public class MythORMTest {
    MythORM mythOrm = MythORM.build(DBType.MYSQL);
    @Test
    public void testClassToTableName(){
        String result = mythOrm.classToTableName("targetDatabase");
        System.out.println(result);
    }
}