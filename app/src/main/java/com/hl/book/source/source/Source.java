package com.hl.book.source.source;

import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.source.download.DownloadPackage;
import com.hl.book.source.result.ParseResult;

public abstract class Source<T extends ParseResult> {
    public String name;
    public String searchUrl;
    public String bookUrl;
    public String chapterUrl;

    public abstract T parseBook(BookBean bookBean);
    public abstract T parseBookDetail(String url);
    public abstract T parseContent(ChapterBean chapterBean);
    public abstract T parseContent(DownloadPackage downloadPackage);
    public abstract T parseSearch(String searchTxt);
}
