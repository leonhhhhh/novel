package com.hl.book.source.source;

import com.hl.book.model.bean.BookBean;
import com.hl.book.source.result.ParseResult;

public abstract class Source {
    public String name;
    public String searchUrl;
    public String bookUrl;
    public String chapterUrl;

    public abstract BookBean parseBook(BookBean bookBean);
    public abstract ParseResult parseBookDetail(String url);
    public abstract ParseResult parseContent(String url);
    public abstract ParseResult parseSearch(String searchTxt);
}
