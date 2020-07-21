package com.hl.book.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.book.R;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.ui.adapter.ChapterListAdapter;
import com.hl.book.base.BookResourceBaseUrl;
import com.hl.book.base.Config;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.util.ActivitySkipUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// TODO: 2020/7/14 书籍详情界面: 把章节列表界面转为书籍详情界面
// TODO: 2020/7/14 书籍详情介绍
// TODO: 2020/7/14 书籍章节显示
// TODO: 2020/7/14 上次阅读章节显示
// TODO: 2020/7/14 继续阅读按钮
// TODO: 2020/7/14 加入移除书架
// TODO: 2020/7/14 缓存章节功能  删除缓存功能
public class ChapterListActivity extends AppCompatActivity implements OnItemClickListener {
    private ChapterListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvAdd;
    private BookBean bookBean;
    private ArrayList<ChapterBean> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);
        bookBean = (BookBean) getIntent().getSerializableExtra("book");
        if (bookBean ==null){
            Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
            finish();
        }
        setTitle(bookBean.name);
        recyclerView = findViewById(R.id.recyclerView);
        tvAdd = findViewById(R.id.tvAdd);
        if (bookBean.hasAdd){
            tvAdd.setText("移除书架");
        }else {
            tvAdd.setText("加入书架");
        }
        data = new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChapterListAdapter(data,this);
        recyclerView.setAdapter(adapter);

        startGetData();
    }
    private void startGetData(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                String url = BookResourceBaseUrl.biquge.BookUrl+ bookBean.url;
                Connection connect = Jsoup.connect(url);
                connect.header("User-Agent", Config.UserAgent);
                try {
                    Document document = connect.get();
                    emitter.onNext(document);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Object value) {
                doBook((Document) value);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
    }
    private void doBook(Document document){
        if (document==null)return;
        Element body = document.body();
        Elements links = body.getElementById("list").getElementsByTag("a");
        for (Element link : links) {
            ChapterBean chapterBean = new ChapterBean();
            chapterBean.url = link.attr("href");
            chapterBean.title = link.text();
            data.add(chapterBean);
        }
        adapter.notifyDataSetChanged();
        scrollLastRead();
    }
    private void scrollLastRead(){
        String lastChapter = bookBean.lastChapter;
        if (lastChapter.equals("")||data==null||data.size()==0){
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).title.equals(lastChapter)){
                recyclerView.scrollToPosition(i);
                adapter.setLastIndex(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chapter_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionMore:
                Collections.reverse(data);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public void onItemClick(View view, int position) {
        ChapterBean chapterBean = data.get(position);
        AppSharedper.getInstance(this).putString(bookBean.name,
                chapterBean.title);
        chapterBean.title = bookBean.name;//设置下个界面的title为书名
        ActivitySkipUtil.skipAct(this,ReadActivity.class
        ,"book", bookBean,"chapterBean",chapterBean);
    }

    public void onAddListener(View view) {
        bookBean.hasAdd = !bookBean.hasAdd;
        if (bookBean.hasAdd){
            DBCenter.getInstance().insertBook(bookBean);
            tvAdd.setText("移除书架");
        }else {
            DBCenter.getInstance().delBook(bookBean);
            tvAdd.setText("加入书架");

        }

    }

    public void onReadListener(View view) {
        ChapterBean chapterBean = data.get(adapter.getLastIndex());
        AppSharedper.getInstance(this).putString(bookBean.name,
                chapterBean.title);
        chapterBean.title = bookBean.name;//设置下个界面的title为书名
        ActivitySkipUtil.skipAct(this,ReadActivity.class
                ,"book", bookBean,"chapterBean",chapterBean);
    }
}
