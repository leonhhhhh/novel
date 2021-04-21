package com.hl.book.source.source;

import com.hl.book.base.BookResourceBaseUrl;
import com.hl.book.base.Config;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.source.Url;
import com.hl.book.source.result.BookDetailResult;
import com.hl.book.source.result.ContentResult;
import com.hl.book.source.result.ParseResult;
import com.hl.book.source.result.SearchResult;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceBiQuGe extends Source {

    public SourceBiQuGe() {
        name = "笔趣阁";
        searchUrl = Url.UrlBiQuGe.SearchUrl;
        bookUrl = Url.UrlBiQuGe.BookUrl;
        chapterUrl = Url.UrlBiQuGe.ChapterUrl;
    }

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
                data.add(chapterBean);
            }
            result.data = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ContentResult parseContent(String url) {
        ContentResult result = new ContentResult();
        Connection connect = Jsoup.connect(url);
        connect.header("User-Agent", Config.UserAgent);
        try {
            Document document = connect.get();
            ChapterBean chapterBean = new ChapterBean();
            result.data = chapterBean;
            if (document == null) {
                return result;
            }
            Element body = document.body();
            Element other = body.getElementsByClass("bookname").first();
            if (other == null) {
                chapterBean.title = "章节出错加载!!!!!!";
                return result;
            }
            chapterBean.textBean.content = body.getElementById("content").html();
            chapterBean.title = other.getElementsByTag("h1").text();
            chapterBean.nextUrl = chapterUrl+other.getElementsByClass("bottem1").first().
                    getElementsByTag("a").get(2).attr("href");
            chapterBean.url = url;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
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
