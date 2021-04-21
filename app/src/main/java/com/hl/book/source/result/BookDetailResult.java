package com.hl.book.source.result;

import com.hl.book.model.bean.ChapterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍详情解析结果
 */
public class BookDetailResult extends ParseResult{
    public List<ChapterBean> data = new ArrayList<>();

}
