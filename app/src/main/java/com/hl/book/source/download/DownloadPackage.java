package com.hl.book.source.download;

import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.source.SourceManager;
import com.hl.book.source.result.ParseResult;
import com.hl.book.source.source.Source;

/**
 * 小说内容下载包
 */
public class DownloadPackage extends ParseResult {
    public ChapterBean chapterBean;
    public BookBean bookBean;
    public Source source;

    public DownloadPackage(ChapterBean chapterBean) {
        this.chapterBean = chapterBean;
        init();
    }
    private void init(){
        if (chapterBean == null){
            return;
        }
        if (bookBean == null){
            bookBean = getBookBean();
        }
        if (source == null){
            source = getSource();
        }
    }
    public boolean check(){
        return chapterBean == null || bookBean == null || source != null;
    }
    private BookBean getBookBean(){
        bookBean = DBCenter.getInstance().getBookByUrl(chapterBean.url);
        return bookBean;
    }
    private Source getSource(){
        source = SourceManager.getInstance().getSourceByLink(chapterBean.bookId);
        return source;
    }
}
