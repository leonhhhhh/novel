package com.hl.book.source.source;

import com.hl.book.base.Config;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.source.Url;
import com.hl.book.source.download.DownloadPackage;
import com.hl.book.source.result.BookDetailResult;
import com.hl.book.source.result.ContentResult;
import com.hl.book.source.result.ParseResult;
import com.hl.book.source.result.SearchResult;
import com.hl.book.util.StrUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceDingDian extends Source {

    public SourceDingDian() {
        name = "顶点小说";
        searchUrl = Url.UrlBiQuGe.SearchUrl;
        bookUrl = Url.UrlBiQuGe.BookUrl;
        chapterUrl = Url.UrlBiQuGe.ChapterUrl;
    }

    /**
     * @param bookBean 小说
     * @return 解析小说详情
     */
    @Override
    public BookBean parseBook(BookBean bookBean) {
        Connection connect = Jsoup.connect(bookBean.url);
        connect.header("User-Agent", Config.UserAgent);
        try {
            Document document = connect.get();
            Element body = document.body();
            bookBean.cover = body.getElementById("fmimg").getElementsByTag("img").attr("src");
            Element info = document.body().getElementById("info");
            bookBean.author = info.getElementsByTag("p").get(0).text().substring(2);
            bookBean.setNewTime(info.getElementsByTag("p").get(2).text().replace("最后更新：",""));
            bookBean.newChapter = info.getElementsByTag("p").get(3).getElementsByTag("a").text();
            if (StrUtil.isEmpty(bookBean.lastChapterUrl)){
                Elements links = body.getElementById("list").getElementsByTag("a");
                if (links != null && links.size() > 0){
                    bookBean.lastChapterUrl = chapterUrl+links.get(0).attr("href");
                    bookBean.lastChapter = links.get(0).text();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookBean;
    }

    @Override
    public ParseResult parseBookDetail(String url) {
        BookDetailResult result = new BookDetailResult();
        List<ChapterBean> data = new ArrayList<>();
        Connection connect = Jsoup.connect(url);
        connect.header("User-Agent", Config.UserAgent);
        try {
            Document document = connect.get();
            Element body = document.body();
            Elements links = body.getElementById("list").getElementsByTag("a");
            for (int i = data.size(); i < links.size(); i++) {
                Element link = links.get(i);
                ChapterBean chapterBean = new ChapterBean();
                chapterBean.bookId = url;
                chapterBean.url = chapterUrl+link.attr("href");
                chapterBean.title = link.text();
                chapterBean.index = i;
                data.add(chapterBean);
            }
            result.data = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ContentResult parseContent(ChapterBean chapterBean) {
        ContentResult result = new ContentResult();
        Connection connect = Jsoup.connect(chapterBean.url);
        connect.header("User-Agent", Config.UserAgent);
        try {
            Document document = connect.get();
            result.data = chapterBean;
            if (document == null) {
                return result;
            }
            Element body = document.body();
            Element other = body.getElementsByClass("bookname").first();
            if (other == null) {
                chapterBean.textBean.content = "章节加载出错";
                return result;
            }
            chapterBean.textBean.content = body.getElementById("content").html();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ParseResult parseContent(DownloadPackage downloadPackage) {
        parseContent(downloadPackage.chapterBean);
        return downloadPackage;
    }

    @Override
    public SearchResult parseSearch(String searchTxt) {
        SearchResult result = new SearchResult();
        ArrayList<BookBean> list = new ArrayList<>();
        String fullUrl = searchUrl + searchTxt;
        Connection connect = Jsoup.connect(fullUrl);
        connect.header("User-Agent", Config.UserAgent);
        try {
            Document document = connect.get();
            Element body = document.body();
            Elements elements = body.getElementsByClass("result-item result-game-item");
            for (Element e : elements) {
                BookBean book = new BookBean();
                book.cover = e.getElementsByTag("img").attr("src");
                book.url = bookUrl+e.getElementsByClass("result-game-item-pic").get(0)
                        .getElementsByTag("a").attr("href");
                book.name = e.getElementsByTag("h3").get(0).getElementsByTag("span").text();
                book.desc = e.getElementsByClass("result-game-item-desc").text();
                book.author = e.getElementsByClass("result-game-item-info-tag").get(0)
                        .getElementsByTag("span").get(1).text();
                book.newChapter = e.getElementsByClass("result-game-item-info-tag").get(3)
                        .getElementsByTag("a").text();
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.data = list;
        return result;
    }
}
