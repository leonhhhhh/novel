package com.hl.book.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hl.book.R;
import com.hl.book.base.BookResourceBaseUrl;
import com.hl.book.base.Config;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.localdata.AppSharedperKeys;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.BookBean;
import com.hl.book.ui.adapter.BookListAdapter;
import com.hl.book.ui.dialog.BookListBottomDialog;
import com.hl.book.ui.dialog.base.DialogMessage;
import com.hl.book.ui.dialog.base.OnDialogListener;
import com.hl.book.util.ActivitySkipUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BookListActivity extends AppCompatActivity implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<BookBean> data;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setContentView(R.layout.activity_book_list);
        iniView();
    }

    private void iniView() {
        data = new ArrayList<>();
        swipeLayout = findViewById(R.id.swipeRefreshLayout);
        swipeLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookListAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    private void loadData() {
        iniData();
        startGetData();
        adapter.notifyDataSetChanged();
    }
    private void iniData() {
        boolean isFirst = AppSharedper.getInstance(this).getBoolean(AppSharedperKeys.IsFirstIn, true);
        if (isFirst) {
            BookBean bookBean  = new BookBean();
            bookBean.name = "沧元图";
            bookBean.url = "41037/";
            bookBean.hasAdd = true;
            data.add(bookBean);
            DBCenter.getInstance().insertBooks(data);
            AppSharedper.getInstance(this).putBoolean(AppSharedperKeys.IsFirstIn, false);
        } else {
            data.clear();
            data.addAll(DBCenter.getInstance().getBooks());
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

    @SuppressLint("CheckResult")
    private void startGetData() {
        Observable.fromIterable(data)
                .subscribeOn(Schedulers.io())
                .map(new Function<BookBean, Object>() {
                    @Override
                    public Object apply(BookBean bookBean) {
                        bookBean.hasAdd = true;
                        doBooks(bookBean);
                        return bookBean;
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
                        DBCenter.getInstance().insertBooks(data);
                        adapter.notifyDataSetChanged();
                        swipeLayout.setRefreshing(false);
                    }
                });
    }
    private void doBooks(BookBean bookBean) {
        String fullUrl = BookResourceBaseUrl.biquge.BookUrl + bookBean.url;
        Connection connect = Jsoup.connect(fullUrl);
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
    }

    @Override
    public void onItemClick(View view, int position) {
        BookBean bookBean = data.get(position);
        bookBean.chick();
        DBCenter.getInstance().updateBook(bookBean);
        adapter.notifyDataSetChanged();
        ActivitySkipUtil.skipAct(this, ChapterListActivity.class
                , "book", bookBean);
    }

    public void onItemSettingListener(View view) {
        final int index = (int) view.getTag();
        final BookBean bookBean = data.get(index);
        if (bookBean ==null)return;
        BookListBottomDialog dialog = new BookListBottomDialog(this);
        dialog.show();
        dialog.setOnDialogListener(new OnDialogListener() {
            @Override
            public void onClick(DialogMessage message) {
                if (message.getResult()!=DialogMessage.RESULT_OK)return;
                if (message.getMessage().equals("del")){
                    DBCenter.getInstance().delBook(data.get(index));
                    data.remove(index);
                    adapter.notifyDataSetChanged();
                }else {
                    ActivitySkipUtil.skipAct(BookListActivity.this, ChapterListActivity.class
                            , "book", bookBean);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
