package com.myth.mysql;

/**
 * Created by Myth on 2017/1/22 0022
 */
public class Table {
    private String name;
    private String type;
    public Table(String name,String type){
        this.name = name;
        this.type = type;
    }
    @Override
    public String toString() {
        return "table [name=" + name + ", type=" + type + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}