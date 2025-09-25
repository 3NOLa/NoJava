package com.util;

public class location{
    int line, column;

    public location(int line, int column){
        this.line = line;
        this.column = column;
    }

    public void reset() {
        this.line = 0;
        this.column = 0;
    }


    public location copy(){
        return new location(this.line, this.column);
    }

    public void update(char c){
        if (c == '\n') {
            this.line++;
            this.column = 0;
        }else{
            this.column++;
        }

    }

    @Override
    public String toString(){
        return "line: " + line + ", column: " + column;
    }
}
