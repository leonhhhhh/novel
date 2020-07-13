package com.hl.book.model;

import java.io.Serializable;

public class Book implements Serializable {
    public String name="";
    public String author="";
    public String url="";
    public String cover="";
    public String lastChapter="";
    public String lastChapterUrl="";
    public String newChapter="";
    public String desc="";

    public Book(){

    }
    public Book(String name,String url){
        this.name = name;
        this.url = url;
    }

}
