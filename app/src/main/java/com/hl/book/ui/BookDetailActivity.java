package com.hl.book.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.book.R;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.source.SourceManager;
import com.hl.book.source.result.BookDetailResult;
import com.hl.book.source.source.Source;
import com.hl.book.ui.adapter.ChapterListAdapter;
import com.hl.book.util.ActivitySkipUtil;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 小说详情界面  包含章节列表
 */
// TODO: 2021/4/22 换源:更新Book表(进度信息重置),插入新的章节记录
    // TODO: 2021/4/22  换源后进度是否可以智能匹配探索
// TODO: 2021/3/17 增加小说详情介绍头部
public class BookDetailActivity extends AppCompatActivity implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ChapterListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvAdd;
    private BookBean bookBean;
    private List<ChapterBean> data;
    private Source currentSource;

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
        SourceManager sourceManager = SourceManager.getInstance();
        currentSource = sourceManager.getSourceByLink(bookBean.url);
        SwipeRefreshLayout swipeLayout = findViewById(R.id.swipeRefreshLayout);
        swipeLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        tvAdd = findViewById(R.id.tvAdd);
        if (bookBean.hasAdd){
            tvAdd.setText("移除书架");
        }else {
            tvAdd.setText("加入书架");
        }
        data = DBCenter.getInstance().getChapterListByBook(bookBean.url);
        if (data == null || data.size() == 0){
            startGetData();
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChapterListAdapter(data,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data.size()>0){
            bookBean = DBCenter.getInstance().getBookByUrl(bookBean.url);
            scrollLastRead();
        }
    }

    private void startGetData(){
        Single.just(bookBean.url)
                .map(new Function<String, BookDetailResult>() {
                    @Override
                    public BookDetailResult apply(String url) {
                        BookDetailResult result = (BookDetailResult) currentSource.parseBookDetail(url);
                        DBCenter.getInstance().insertChapters(result.data);
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BookDetailResult>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BookDetailResult o) {
                        data.clear();
                        data.addAll(o.data);
                        adapter.notifyDataSetChanged();
                        scrollLastRead();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
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
        if (item.getItemId() == R.id.actionMore) {
            Collections.reverse(data);
            adapter.notifyDataSetChanged();
        }
        return true;
    }
    @Override
    public void onItemClick(View view, int position) {
        ChapterBean chapterBean = data.get(position);
        ActivitySkipUtil.skipAct(this,ReadActivity.class
        ,"book", bookBean,"chapterBean",chapterBean);
        finish();
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

    @Override
    public void onRefresh() {
        startGetData();
    }
}
