package com.hl.book.source.result;

import com.hl.book.model.bean.ChapterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 章节内容解析结果
 */
public class ContentResult extends ParseResult{
    public ChapterBean data = new ChapterBean();
    public boolean isLoading = false;
}
