package com.hl.book.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

@Entity(nameInDb = "ReadHistory",indexes = {@Index(value = "name,author", unique = true)})
public class ReadHistoryBean implements Serializable {
    @Transient
    private static final long serialVersionUID = 0;
    @Transient
    public boolean hasAdd = false;
    public String name="";
    public String author="";
    @Id
    public String url="";
    public String cover="";
    public String lastChapter="";
    public String lastChapterUrl="";
    public int lastChapterProgress=0;
    public String newChapter="";
    public String newTime="";
    public String newShowTime="";
    public String desc="";
    @Generated(hash = 1375119056)
    public ReadHistoryBean(String name, String author, String url, String cover,
            String lastChapter, String lastChapterUrl, int lastChapterProgress,
            String newChapter, String newTime, String newShowTime, String desc) {
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
    }
    @Generated(hash = 538705111)
    public ReadHistoryBean() {
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
    public int getLastChapterProgress() {
        return this.lastChapterProgress;
    }
    public void setLastChapterProgress(int lastChapterProgress) {
        this.lastChapterProgress = lastChapterProgress;
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
    public void setNewTime(String newTime) {
        this.newTime = newTime;
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


}
