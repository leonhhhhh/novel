package com.hl.book;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.hl.book.adapter.ChapterListAdapter;
import com.hl.book.base.BookResourceBaseUrl;
import com.hl.book.base.Config;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.model.Book;
import com.hl.book.model.Chapter;
import com.hl.book.util.ActivitySkipUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// TODO: 2020/7/9 倒序正序做一个按钮
public class ChapterListActivity extends AppCompatActivity implements OnItemClickListener {
    private ChapterListAdapter adapter;
    private RecyclerView recyclerView;
    private Book book;
    private ArrayList<Chapter> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);
        book = (Book) getIntent().getSerializableExtra("book");
        if (book==null){
            Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
            finish();
        }
        setTitle(book.name);
        recyclerView = findViewById(R.id.recyclerView);
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
                String url = BookResourceBaseUrl.biquge.BookUrl+book.url;
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
            Chapter chapter = new Chapter();
            chapter.url = link.attr("href");
            chapter.title = link.text();
            data.add(chapter);
        }
        adapter.notifyDataSetChanged();
        scrollLastRead();
    }
    private void scrollLastRead(){
        String lastChapter = AppSharedper.getInstance(this).getString(book.name,"");
        if (lastChapter.equals("")||data==null||data.size()==0){
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).title.equals(lastChapter)){
                recyclerView.scrollToPosition(i);
                adapter.setLastIndex(i);
                adapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        Chapter chapter = data.get(position);
        AppSharedper.getInstance(this).putString(book.name,
                chapter.title);
        chapter.title = book.name;//设置下个界面的title为书名
        ActivitySkipUtil.skipAct(this,ReadActivity.class
        ,"chapter",chapter);
    }
}
