package com.hl.book;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.book.adapter.ReadAdapter;
import com.hl.book.base.BookResourceBaseUrl;
import com.hl.book.base.Config;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.model.Chapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReadActivity extends AppCompatActivity implements OnItemClickListener {
    private ReadAdapter adapter;
    private ArrayList<Chapter> data;
    private RecyclerView recyclerView;
    private Chapter chapter;
    private TextView tvFontSize;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        chapter = (Chapter) getIntent().getSerializableExtra("chapter");
        if (chapter ==null){
            Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
            finish();
        }
        assert chapter != null;
        setTitle(chapter.title);

        recyclerView = findViewById(R.id.recyclerView);
        tvFontSize = findViewById(R.id.tvFontSize);
        int fontSize = AppSharedper.getInstance(this).getInt("fontSize",12);
        tvFontSize.setText(fontSize+"");
        iniListener();
        data = new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ReadAdapter(data,this);
        recyclerView.setAdapter(adapter);
        adapter.setTextSize(fontSize);
        startGetContent(chapter.url);
    }
    private void iniListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.computeVerticalScrollRange()-recyclerView.computeVerticalScrollOffset()<
                        recyclerView.computeVerticalScrollExtent()*5) {
                    System.out.println("缓存下一页!!!!");
                    Chapter last = data.get(data.size()-1);
                    startGetContent(last.url);
                    AppSharedper.getInstance(ReadActivity.this).putString(chapter.title,
                            last.title);
                }
            }
        });
    }
    private boolean isLoading = false;
    private void startGetContent(String url){
        if (isLoading){
            return;
        }
        isLoading = true;
        final String fullUrl = BookResourceBaseUrl.biquge.ChapterUrl +url;
                Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                Connection connect = Jsoup.connect(fullUrl);
                connect.header("User-Agent", Config.UserAgent);
                try {
                    Document document = connect.get();
                    emitter.onNext(document);
                } catch (IOException e) {
                    e.printStackTrace();
                    isLoading = false;
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
                doContent((Document) value);
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
    private void doContent(Document document){
        if (document==null){
            isLoading = false;
            return;
        }
        Element body = document.body();
        Chapter chapter = new Chapter();
        Element other = body.getElementsByClass("bookname").first();
        if (other==null){
            chapter.title = "章节出错加载!!!!!!";
        }else {
            chapter.content = body.getElementById("content").html();
            chapter.title = other.getElementsByTag("h1").text();
            chapter.url = other.getElementsByClass("bottem1").first().
                    getElementsByTag("a").get(2).attr("href");
            data.add(chapter);
            adapter.notifyDataSetChanged();
            // TODO: 2020/7/9 如果当前已经看了很多  则需要把前面的回收掉
        }
        isLoading = false;
    }

    @Override
    public void onItemClick(View view, int position) {
        recyclerView.scrollBy(0,recyclerView.computeVerticalScrollExtent()-recyclerView.computeVerticalScrollExtent()/10);
    }

    public void onSettingListener(View view) {
        view.setSelected(!view.isSelected());
        if (view.isSelected()){
            findViewById(R.id.llyBottom).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.llyBottom).setVisibility(View.GONE);
        }
    }

    public void onFontSizeUpListener(View view) {
        int fontSize = AppSharedper.getInstance(this).getInt("fontSize",12);
        fontSize++;
        AppSharedper.getInstance(this).putInt("fontSize",fontSize);
        tvFontSize.setText(fontSize+"");
        adapter.setTextSize(fontSize);
    }

    public void onFontSizeDownListener(View view) {
        int fontSize = AppSharedper.getInstance(this).getInt("fontSize",12);
        fontSize--;
        AppSharedper.getInstance(this).putInt("fontSize",fontSize);
        tvFontSize.setText(fontSize+"");
        adapter.setTextSize(fontSize);
    }

    public void onDayListener(View view) {
        adapter.setNight(false);
        recyclerView.setBackgroundColor(Color.WHITE);
    }

    public void onNightListener(View view) {
        adapter.setNight(true);
        recyclerView.setBackgroundColor(Color.BLACK);
    }
}