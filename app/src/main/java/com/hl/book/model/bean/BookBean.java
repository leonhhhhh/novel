package com.hl.book.model.bean;

import android.support.annotation.NonNull;

import com.hl.book.util.DateUtil;
import com.hl.book.util.StrUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(nameInDb = "Book",indexes = {@Index(value = "name,author", unique = true)})
public class BookBean implements Serializable {
    @Transient
    private static final long serialVersionUID = 0;
    @Transient
    public boolean hasAdd = false;
    /**
     * 小说名
     */
    public String name="";
    /**
     * 小说作者
     */
    public String author="";
    /**
     * 小说详情Path
     */
    @Id
    public String url="";
    /**
     * 封面图
     */
    public String cover="";
    /**
     * 最后阅读章节名称
     */
    public String lastChapter="";
    /**
     * 最后阅读章节Path
     */
    public String lastChapterUrl="";
    /**
     * 最后阅读章节进度
     */
    public int lastChapterProgress=0;
    /**
     * 最新章节名称
     */
    public String newChapter="";
    /**
     * 最新章节更新时间
     */
    public String newTime="";
    /**
     * 最新章节更新显示时间
     */
    public String newShowTime="";
    /**
     * 简介
     */
    public String desc="";
    /**
     * 点击事件
     */
    public long chickTime=0;


    @Generated(hash = 2004827972)
    public BookBean(String name, String author, String url, String cover, String lastChapter,
            String lastChapterUrl, int lastChapterProgress, String newChapter, String newTime,
            String newShowTime, String desc, long chickTime) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.cover = cover;
        this.lastChapter = lastChapter;
        this.lastChapterUrl = lastChapterUrl;
        this.lastChapterProgress = lastChapterProgress;
        this.newChapter = newChapter;
        this.newTime = newTime;
        this.newShowTime = newShowTime;
        this.desc = desc;
        this.chickTime = chickTime;
    }
    @Generated(hash = 269018259)
    public BookBean() {
    }


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCover() {
        return this.cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getLastChapter() {
        return this.lastChapter;
    }
    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }
    public String getLastChapterUrl() {
        return this.lastChapterUrl;
    }
    public void setLastChapterUrl(String lastChapterUrl) {
        this.lastChapterUrl = lastChapterUrl;
    }
    public String getNewChapter() {
        return this.newChapter;
    }
    public void setNewChapter(String newChapter) {
        this.newChapter = newChapter;
    }
    public String getNewTime() {
        return this.newTime;
    }
    public String getNewShowTime() {
        return this.newShowTime;
    }
    public void setNewShowTime(String newShowTime) {
        this.newShowTime = newShowTime;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public long getChickTime() {
        return this.chickTime;
    }
    public void setChickTime(long chickTime) {
        this.chickTime = chickTime;
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
        if (StrUtil.isEmpty(newTime)){
            return false;
        }
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
    public int getLastChapterProgress() {
        return this.lastChapterProgress;
    }
    public void setLastChapterProgress(int lastChapterProgress) {
        this.lastChapterProgress = lastChapterProgress;
    }

    @NonNull
    @Override
    public String toString() {
        return "BookBean{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
