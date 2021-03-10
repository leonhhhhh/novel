package com.hl.book.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "Chapter",indexes = {@Index(value = "bookId,url", unique = true)})
public class ChapterBean implements Serializable {
    @Transient
    private static final long serialVersionUID = 1;
    /**
     * 所属小说的Path(在每个小说网站应该是非重的)
     */
    @Property(nameInDb = "book_id")
    public String bookId="";
    /**
     * 章节Path
     */
    public String url="";
    /**
     * 章节标题
     */
    public String title="";
    /**
     *是否缓存
     */
    @Property(nameInDb = "has_download")
    public Integer hasDownload=0;
    /**
     * 下一章Path
     */
    @Transient
    public String nextUrl="";
    /**
     * 内容
     */
    @Transient
    public TextBean textBean = new TextBean();

    @Generated(hash = 960325266)
    public ChapterBean(String bookId, String url, String title, Integer hasDownload) {
        this.bookId = bookId;
        this.url = url;
        this.title = title;
        this.hasDownload = hasDownload;
    }
    @Generated(hash = 1028095945)
    public ChapterBean() {
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getNextUrl() {
        return this.nextUrl;
    }
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
    public Integer getHasDownload() {
        return this.hasDownload;
    }
    public void setHasDownload(Integer hasDownload) {
        this.hasDownload = hasDownload;
    }



}
