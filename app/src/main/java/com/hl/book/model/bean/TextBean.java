package com.hl.book.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

@Entity(nameInDb = "Text")
public class TextBean implements Serializable {
    @Transient
    private static final long serialVersionUID = 3333;
    /**
     * 内容id(章节完整路径)
     */
    @Property(nameInDb = "text_id")
    @Id
    public String textId="";
    /**
     * 章节内容
     */
    public String content="";
    @Generated(hash = 851289446)
    public TextBean(String textId, String content) {
        this.textId = textId;
        this.content = content;
    }
    @Generated(hash = 809912491)
    public TextBean() {
    }
    public String getTextId() {
        return this.textId;
    }
    public void setTextId(String textId) {
        this.textId = textId;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
