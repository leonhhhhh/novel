package com.hl.book;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hl.book.adapter.BookListAdapter;
import com.hl.book.base.BookResourceBaseUrl;
import com.hl.book.base.Config;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.localdata.AppSharedperKeys;
import com.hl.book.model.Book;
import com.hl.book.util.ActivitySkipUtil;
import com.hl.book.util.net.JsonUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// TODO: 2020/7/14 书籍列表项增加更多按钮 点击弹底部窗口
// TODO: 2020/7/14 底部窗口包括:书籍简略显示  书籍详情界面跳转按钮 删除  
// TODO: 2020/7/14 下拉刷新功能 
// TODO: 2020/7/14 更新未读红点提示 
// TODO: 2020/7/14 更新时间显示
// TODO: 2020/7/14 增加数据库支持
public class BookListActivity extends AppCompatActivity implements OnItemClickListener {
    private ArrayList<Book> data;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setContentView(R.layout.activity_book_list);
        iniData();
        iniView();
        startGetData();
    }

    private void iniView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookListAdapter(data, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void iniData() {
        data = new ArrayList<>();
        boolean isFirst = AppSharedper.getInstance(this).getBoolean(AppSharedperKeys.IsFirstIn, true);
        if (isFirst) {
            Book book = new Book("离天大圣", "37299/");
//            data.add(book);
//            book = new Book("万千之心", "43283/");
//            data.add(book);
            book = new Book("沧元图", "41037/");
//            data.add(book);
//            book = new Book("超神机械师", "29105/");
//            data.add(book);
//            book = new Book("峡谷正能量", "40918/");
//            data.add(book);
//            book = new Book("小阁老", "43022/");
            data.add(book);
            saveBooks(data);
            AppSharedper.getInstance(this).putBoolean(AppSharedperKeys.IsFirstIn, false);
        } else {
            String json = AppSharedper.getInstance(this).getString("books", "");
            Logger.i(json != null ? json : "");
            data = JsonUtil.getGson().fromJson(json, new TypeToken<List<Book>>() {
            }.getType());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionMore:
                ActivitySkipUtil.skipAct(BookListActivity.this,SearchActivity.class);
                break;
            default:
                break;
        }
        return true;
    }

    private void saveBooks(ArrayList<Book> books) {
        String json = JsonUtil.toJson(books);
        Logger.i(json != null ? json : "");
        AppSharedper.getInstance(this).putString("books", json);
    }

    @SuppressLint("CheckResult")
    private void startGetData() {
        Observable.fromIterable(data)
                .subscribeOn(Schedulers.io())
                .map(new Function<Book, Object>() {
                    @Override
                    public Object apply(Book book) {
                        doBooks(book);
                        return book;
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
                        saveBooks(data);
                        adapter.notifyDataSetChanged();
                    }

                });
    }
    private void doBooks(Book book) {
        String fullUrl = BookResourceBaseUrl.biquge.BookUrl + book.url;
        Connection connect = Jsoup.connect(fullUrl);
        connect.header("User-Agent", Config.UserAgent);
        try {
            Document document = connect.get();
            Element body = document.body();
            book.cover = body.getElementById("fmimg").getElementsByTag("img").attr("src");
            Element info = document.body().getElementById("info");
            book.author = info.getElementsByTag("p").get(0).text().substring(2);
            book.newChapter = info.getElementsByTag("p").get(3).getElementsByTag("a").text();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Book book = data.get(position);
        ActivitySkipUtil.skipAct(this, ChapterListActivity.class
                , "book", book);
    }
}
