package com.hl.book.source.result;

import com.hl.book.model.bean.BookBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索解析结果
 */
public class SearchResult extends ParseResult{
    public List<BookBean> data = new ArrayList<>();
    public String nextPage = "";

}
