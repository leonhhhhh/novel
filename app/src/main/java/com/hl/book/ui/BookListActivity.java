package com.hl.book.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hl.book.BuildConfig;
import com.hl.book.R;
import com.hl.book.base.BaseActivity;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.localdata.AppSharedperKeys;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.source.SourceManager;
import com.hl.book.source.source.Source;
import com.hl.book.ui.adapter.BookListAdapter;
import com.hl.book.ui.dialog.BookListBottomDialog;
import com.hl.book.ui.dialog.base.DialogMessage;
import com.hl.book.ui.dialog.base.OnDialogListener;
import com.hl.book.util.ActivitySkipUtil;
import com.hl.book.util.StrUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 小说列表 界面
 */
// TODO: 2021/4/22 获取Book详情时 将书籍属性和章节列表都入库 在书籍详情页即可不用再请求了,除非搜索过去或者主动刷新
// TODO: 2021/5/1 引入事件总线+获取小说信息封装成单例类 共享数据分发数据
public class BookListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
//    private static final String TAG = "BookListActivity";
    private ArrayList<BookBean> data;
    private BookListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private SourceManager sourceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setContentView(R.layout.activity_book_list);
        sourceManager = SourceManager.getInstance();
        iniView();
    }

    private void iniView() {
        data = new ArrayList<>();

        swipeLayout = findViewById(R.id.swipeRefreshLayout);
        swipeLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookListAdapter(data, new OnItemClickListener());
        recyclerView.setAdapter(adapter);
        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG){
//            finish();
        }
    }

    private void loadData() {
        iniData();
        startGetData();
        adapter.notifyDataSetChanged();
    }
    private void iniData() {
        boolean isFirst = AppSharedper.getInstance(this).getBoolean(AppSharedperKeys.IsFirstIn, true);
        if (isFirst) {
            AppSharedper.getInstance(this).putBoolean(AppSharedperKeys.IsFirstIn, false);
        }
        data.clear();
        data.addAll(DBCenter.getInstance().getBooks());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionMore) {
            ActivitySkipUtil.skipAct(BookListActivity.this, SearchActivity.class);
        }
        return true;
    }

    @SuppressLint("CheckResult")
    private void startGetData() {
        Disposable subscribe = Flowable.fromIterable(data)
                .parallel()
                .runOn(Schedulers.io())
                .map(new Function<BookBean, BookBean>() {
                    @Override
                    public BookBean apply(BookBean bookBean) {
                        Logger.v("解析书籍:"+bookBean.name);
                        bookBean.hasAdd = true;
                        Source source = sourceManager.getSourceByLink(bookBean.url);
                        source.parseBook(bookBean);
                        return bookBean;
                    }
                })
                .sequential()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookBean>() {
                    @Override
                    public void accept(BookBean bookBean) {
                        Logger.v(bookBean.name + "解析完毕:");
                        adapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Logger.v("全部更新完毕:");
                        DBCenter.getInstance().insertBooks(data);
                        swipeLayout.setRefreshing(false);
                    }
                });

//
//        Observable.fromIterable(data)
//                .subscribeOn(Schedulers.io())
//                .map(new Function<BookBean, Object>() {
//                    @Override
//                    public Object apply(BookBean bookBean) {
//                        bookBean.hasAdd = true;
//                        Source source = sourceManager.getSourceByLink(bookBean.url);
//                        Logger.v("获取书籍信息:"+bookBean.toString());
//                        source.parseBook(bookBean);
//                        return bookBean;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(Object value) {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        DBCenter.getInstance().insertBooks(data);
//                        adapter.notifyDataSetChanged();
//                        swipeLayout.setRefreshing(false);
//                    }
//                });
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
                    ActivitySkipUtil.skipAct(BookListActivity.this, BookDetailActivity.class
                            , "book", bookBean);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    class OnItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(v);
            int position = vh.getAdapterPosition();
            if (position<0 || position>=data.size()) {
                return;
            }
            BookBean bookBean = data.get(position);
            bookBean.chick();
            DBCenter.getInstance().updateBook(bookBean);
            adapter.notifyDataSetChanged();

            if (!StrUtil.isEmpty(bookBean.lastChapterUrl)){
                ChapterBean chapterBean = DBCenter.getInstance().getChapterByChapterUrl(bookBean.lastChapterUrl);
                if (chapterBean!=null){
                    ActivitySkipUtil.skipAct(BookListActivity.this,ReadActivity.class
                            ,"book", bookBean,"chapterBean",chapterBean);
                    return;
                }
            }
            ActivitySkipUtil.skipAct(BookListActivity.this, BookDetailActivity.class
                    , "book", bookBean);
        }
    }


}
