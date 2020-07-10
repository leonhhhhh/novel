package com.hl.book;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookListActivity extends AppCompatActivity implements OnItemClickListener {
    private final String TAG = "BookListActivity";
    private ArrayList<Book> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        iniData();
        iniView();

    }
    private void iniView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new BookListAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    private void iniData(){
        data = new ArrayList<>();
        boolean isFirst = AppSharedper.getInstance(this).getBoolean(AppSharedperKeys.IsFirstIn,true);
        if (isFirst){
            Book book = new Book("离天大圣","37299/");
            data.add(book);
            book = new Book("万千之心","43283/");
            data.add(book);
            book = new Book("沧元图","41037/");
            data.add(book);
            book = new Book("超神机械师","29105/");
            data.add(book);
            book = new Book("峡谷正能量","40918/");
            data.add(book);
            book = new Book("小阁老","43022/");
            data.add(book);
            saveBooks(data);
            AppSharedper.getInstance(this).putBoolean(AppSharedperKeys.IsFirstIn,false);
        }else {
            String json = AppSharedper.getInstance(this).getString("books","");
            Log.e(TAG, json != null ? json : "");
            data = JsonUtil.getGson().fromJson(json, new TypeToken<List<Book>>() {
            }.getType());
        }
    }
    private void saveBooks(ArrayList<Book> books){
        String json = JsonUtil.toJson(books);
        Log.e(TAG, json != null ? json : "");
        AppSharedper.getInstance(this).putString("books",json);
    }
//    private void startGetData(){
//        Observable.create(new ObservableOnSubscribe<Object>() {
//            @Override
//            public void subscribe(ObservableEmitter<Object> emitter) {
//                String url = BookResourceBaseUrl.biquge.BookUrl+book.url;
//                Connection connect = Jsoup.connect(url);
//                connect.header("User-Agent", Config.UserAgent);
//                try {
//                    Document document = connect.get();
//                    emitter.onNext(document);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//                    @Override
//                    public void onNext(Object value) {
//                        doBook((Document) value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
//    }
    @Override
    public void onItemClick(View view, int position) {
        Book book = data.get(position);
        ActivitySkipUtil.skipAct(this,ChapterListActivity.class
        ,"book",book);
    }

    public void onGoNewListener(View view) {

    }


    public void onGoLastListener(View view) {

    }
}
