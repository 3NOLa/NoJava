package com.util;

public class location{
    int line, column;

    public location(){
        this.line = 1;
        this.column = 1;
    }

    public location(int line, int column){
        this.line = line;
        this.column = column;
    }

    public void reset() {
        this.line = 1;
        this.column = 1;
    }


    public location copy(){
        return new location(this.line, this.column);
    }

    public void newLine(){
        this.line++;
        this.column = 0;
    }

    public void update(){
        this.column++;
    }

    @Override
    public String toString(){
        return "line: " + line + ", column: " + column;
    }
}
