package com.hl.book.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "Chapter",indexes = {@Index(value = "bookId,url", unique = true)})
public class ChapterBean implements Serializable {
    @Transient
    private static final long serialVersionUID = 1;
    @Transient
    public static final int DOWNLOADED = 1;
    @Transient
    public static final int NOT_DOWNLOADED = 0;
    /**
     * 所属小说的完整链接(在每个小说网站应该是非重的)
     */
    @Property(nameInDb = "book_id")
    public String bookId="";
    /**
     * 章节完整路径
     */
    @Id
    public String url="";
    /**
     * 章节标题
     */
    public String title="";
    /**
     *是否缓存
     */
    @Property(nameInDb = "has_downloaded")
    public Integer hasDownloaded=NOT_DOWNLOADED;
    /**
     * 排序索引
     */
    public int index = 0;
    /**
     * 内容
     */
    @Transient
    public TextBean textBean = new TextBean();
    @Generated(hash = 978858919)
    public ChapterBean(String bookId, String url, String title, Integer hasDownloaded,
            int index) {
        this.bookId = bookId;
        this.url = url;
        this.title = title;
        this.hasDownloaded = hasDownloaded;
        this.index = index;
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
    public Integer getHasDownloaded() {
        return this.hasDownloaded;
    }
    public void setHasDownloaded(Integer hasDownloaded) {
        this.hasDownloaded = hasDownloaded;
    }
    public int getIndex() {
        return this.index;
    }
    public void setIndex(int index) {
        this.index = index;
    }



}
