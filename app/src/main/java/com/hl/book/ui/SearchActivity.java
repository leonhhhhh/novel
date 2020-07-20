package com.hl.book.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.hl.book.R;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.ui.adapter.SearchAdapter;
import com.hl.book.base.BookResourceBaseUrl;
import com.hl.book.base.Config;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.model.bean.BookBean;
import com.hl.book.util.ActivitySkipUtil;
import com.hl.book.util.net.JsonUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// TODO: 2020/7/13 搜索结果有多页的情况未处理
// TODO: 2020/7/14  搜索结果与已加入的书籍进行匹配 初始化是否加入书架
public class SearchActivity extends AppCompatActivity implements OnItemClickListener {
    private ArrayList<BookBean> data;
    private RecyclerView.Adapter adapter;
    private EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setContentView(R.layout.activity_search);
        data = new ArrayList<>();
        iniView();
    }

    private void iniView() {
        etSearch = findViewById(R.id.etSearch);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    private void delBooks(BookBean book) {
        DBCenter.getInstance().delBook(book);
        book.hasAdd = false;
        adapter.notifyDataSetChanged();
    }
    private void addBooks(BookBean book) {
        book.hasAdd = true;
        DBCenter.getInstance().insertBook(book);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("CheckResult")
    private void startGetData(final String search) {
        Observable.just(search)
            .subscribeOn(Schedulers.io())
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String search) {
                        doBooks(search);
                        return 0;
                    }
                })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Object value) {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    adapter.notifyDataSetChanged();
                }

            });
    }
    private void doBooks(String search) {
        String fullUrl = BookResourceBaseUrl.biquge.SearchUrl + search;
        Connection connect = Jsoup.connect(fullUrl);
        connect.header("User-Agent", Config.UserAgent);
        try {
            Document document = connect.get();
            Element body = document.body();
            Elements elements = body.getElementsByClass("result-item result-game-item");
            data.clear();
            for (Element e : elements) {
                BookBean book = new BookBean();
                book.cover = e.getElementsByTag("img").attr("src");
                book.url = e.getElementsByClass("result-game-item-pic").get(0)
                        .getElementsByTag("a").attr("href");
                book.url = book.url.replace("/book/","");
                book.name = e.getElementsByTag("h3").get(0).getElementsByTag("span").text();
                book.desc = e.getElementsByClass("result-game-item-desc").text();
                book.author = e.getElementsByClass("result-game-item-info-tag").get(0)
                        .getElementsByTag("span").get(1).text();
                book.newChapter = e.getElementsByClass("result-game-item-info-tag").get(3)
                        .getElementsByTag("a").text();
                data.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        BookBean bookBean = data.get(position);
        ActivitySkipUtil.skipAct(this, ChapterListActivity.class
                , "bookBean", bookBean);
    }

    public void onSearchListener(View view) {
        String search = etSearch.getText().toString().trim();
        if (search.equals("")){
            return;
        }
        startGetData(search);
    }
    public void onAddListener(View view) {
        int index = (int) view.getTag();
        BookBean book = data.get(index);
        if (book.hasAdd){
            delBooks(book);
        }else {
            addBooks(book);
        }
    }
}
