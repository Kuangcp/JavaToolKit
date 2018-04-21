package com.myth.reflect.orm.base;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-4-21  下午7:08
 */
public enum DBType {
    MYSQL("mysql"), POSTGRESQL("postgresql");


    private String type;

    DBType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
