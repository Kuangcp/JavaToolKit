package com.myth.mysql;

/**
 * @author  Myth
 * @date 2016年8月18日 下午10:13:27
 * @TODO 这个类是为了处理表格的位置，能够得到准确的定位
 */
public class Position {
    private int col;
    private int row;
    public Position (){}
    public Position ( int row, int col){
        this.col = col;
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    @Override
    public String toString() {
        return "<" + row + "," + col + "> ";
    }
    /**
     * 重写的equals要注意是一定能做到将对象的值相同的判定为相等，不能有任何错误
     */
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(this == obj) return true; //如果就是当时new的对象，当然是true了
        if(!(obj instanceof Position)) return false;//如果类型都对不上，当然要返回false了
        Position key = (Position)obj;//将进来的Object进行强转，因为上面已经筛选掉了类型不对的情况，这里可以放心强转
        return row== key.row && col == key.col; //自定义的比对方案

    }
    /**
     * 重写的HashCode方法要特别注意key的唯一性，一旦有重复，就是个BUG了
     */
    @Override
    public int hashCode() {
        int result = row;
        result = 37*result+col;
        return result;
    }
}
