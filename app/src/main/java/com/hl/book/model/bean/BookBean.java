package com.hl.book.model.bean;

import com.hl.book.util.DateUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookBean implements Serializable {
    public String name="";
    public String author="";
    public String url="";
    public String cover="";
    public String lastChapter="";
    public String lastChapterUrl="";
    public String newChapter="";
    public String newTime="";
    public String newShowTime="";
    public long chickTime = 0;
    public String desc="";

    public BookBean(){

    }
    public BookBean(String name, String url){
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

    /**
     * 点击时间刷新
     */
    public void chick(){
        chickTime = System.currentTimeMillis();
    }
    public boolean isShowChickPoint(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(newTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date==null)return false;
        return  date.getTime()>chickTime;
    }
}
