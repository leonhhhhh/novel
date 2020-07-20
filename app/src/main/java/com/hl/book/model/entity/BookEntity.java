package com.hl.book.model.entity;

import com.hl.book.util.DateUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookEntity {
    public int id;
    public String name="";
    public String author="";
    public String url="";
    public String cover="";
    public String lastChapter="";
    public String lastChapterUrl="";
    public String newChapter="";
    public String newTime="";
    public String newShowTime="";
    public String desc="";

    public BookEntity(){

    }
    public BookEntity(String name, String url){
        this.name = name;
        this.url = url;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(newTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        newShowTime = DateUtil.format(date);

    }

}
