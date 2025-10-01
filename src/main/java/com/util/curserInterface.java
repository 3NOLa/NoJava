package com.util;

public interface curserInterface <T>{
    public T peek();
    public T get();
    public void consume();
    public boolean isEOF();
}
