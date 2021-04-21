package com.hl.book.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hl.book.R;
import com.hl.book.listener.OnItemClickListener;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.BookBean;
import com.hl.book.source.SourceManager;
import com.hl.book.source.result.SearchResult;
import com.hl.book.source.source.Source;
import com.hl.book.ui.adapter.SearchAdapter;
import com.hl.book.util.ActivitySkipUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// TODO: 2021/2/15 搜索历史 
// TODO: 2021/2/15 分页功能
public class SearchActivity extends AppCompatActivity implements OnItemClickListener {
    private static final String TAG = "SearchActivity";
    private ArrayList<BookBean> data;
    private RecyclerView.Adapter adapter;
    private EditText etSearch;
    private SourceManager sourceManager;
    private Source currentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setContentView(R.layout.activity_search);
        data = new ArrayList<>();
        sourceManager = SourceManager.getInstance();
        currentSource = sourceManager.getDefaultSource();
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
        Single.just(search)
            .map(new Function<String, SearchResult>() {
                @Override
                public SearchResult apply(String search) {
                    return (SearchResult) currentSource.parseSearch(search);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<SearchResult>() {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(SearchResult o) {
                    data.addAll(o.data);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {

                }
            });
    }

    @Override
    public void onItemClick(View view, int position) {
        BookBean bookBean = data.get(position);
        ActivitySkipUtil.skipAct(this, BookDetailActivity.class
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_search, menu);
        sourceManager.getSourceList();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionMore:
                Toast.makeText(SearchActivity.this,"切换源",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
