package com.hl.book.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

// TODO: 2020/7/14 章节下载
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
        data.addAll(DBCenter.getInstance().getChapterListByBook(bookBean.url));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChapterListAdapter(data,this);
        recyclerView.setAdapter(adapter);

        startGetData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data.size()>0){
            bookBean = DBCenter.getInstance().getBook(bookBean.url);
            scrollLastRead();
        }
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
        boolean needRef = data.size() != links.size();
        for (int i = data.size(); i < links.size(); i++) {
            Element link = links.get(i);
            ChapterBean chapterBean = new ChapterBean();
            chapterBean.bookId = bookBean.url;
            chapterBean.url = link.attr("href");
            chapterBean.title = link.text();
            data.add(chapterBean);
        }

        if (needRef){
            adapter.notifyDataSetChanged();
        }
        DBCenter.getInstance().insertChapters(data);
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
        ActivitySkipUtil.skipAct(this,ReadActivity.class
                ,"book", bookBean,"chapterBean",chapterBean);
    }
}
