package com.myth.mysql;

/**
 * Created by Myth on 2017/1/22 0022
 */
public class table {
    String name;
    String type;
    public table(String name,String type){
        this.name = name;
        this.type = type;
    }
    @Override
    public String toString() {
            return "table [name=" + name + ", type=" + type + "]";
        }
}
