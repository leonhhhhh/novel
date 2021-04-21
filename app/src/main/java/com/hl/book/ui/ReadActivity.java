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
import android.widget.TextView;
import android.widget.Toast;

import com.hl.book.R;
import com.hl.book.listener.ReadClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.source.SourceManager;
import com.hl.book.source.result.ContentResult;
import com.hl.book.source.source.Source;
import com.hl.book.ui.adapter.ReadAdapter;
import com.hl.book.ui.view.ReadClickView;
import com.orhanobut.logger.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// TODO: 2021/2/15 缓存小说
public class ReadActivity extends AppCompatActivity implements ReadClickListener {
    private ReadAdapter adapter;
    private ArrayList<ChapterBean> data;
    private RecyclerView recyclerView;
    private TextView tvTitle;
    private ChapterBean chapterBean;
    private BookBean bookBean;
    private TextView tvFontSize;
    private View llyBottom;
    private boolean needScrollLastProcess = false;
    private Source currentSource;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        bookBean = (BookBean) getIntent().getSerializableExtra("book");
        chapterBean = (ChapterBean) getIntent().getSerializableExtra("chapterBean");
        if (chapterBean == null || bookBean == null) {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        SourceManager sourceManager = SourceManager.getInstance();
        currentSource = sourceManager.getSourceByLink(bookBean.url);

        if (bookBean.lastChapterUrl.equals(chapterBean.url) && bookBean.lastChapterProgress>0){
            needScrollLastProcess = true;
        }
        bookBean.lastChapter = chapterBean.title;
        bookBean.lastChapterUrl = chapterBean.url;
        DBCenter.getInstance().updateBook(bookBean);
        setTitle(bookBean.name);
        ReadClickView readClickView = findViewById(R.id.readClickView);
        readClickView.setClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(chapterBean.title);
        llyBottom = findViewById(R.id.llySetting);
        tvFontSize = findViewById(R.id.tvFontSize);
        int fontSize = AppSharedper.getInstance(this).getInt("fontSize", 14);
        tvFontSize.setText(MessageFormat.format("{0}", fontSize));
        iniListener();
        data = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReadAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setTextSize(fontSize);
        boolean isNight = AppSharedper.getInstance(this).getBoolean("isNight", false);
        if (!isNight) {
            onDayListener(null);
        } else {
            onNightListener(null);
        }
        startGetContent(chapterBean.url);
    }
    View currentView;
    StringBuilder builder = new StringBuilder();

    private void iniListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getChildCount() <= 0) {
                    return;
                }
                currentView = recyclerView.getChildAt(0);
                int currentPosition = ((RecyclerView.LayoutParams) currentView.getLayoutParams()).getViewAdapterPosition();
                chapterBean = data.get(currentPosition);
                tvTitle.setText(chapterBean.title);
                builder.delete(0,builder.length());
                builder.append("\n当前item:");
                builder.append(currentPosition);
                builder.append("\n当前界面总长度:");
                builder.append(recyclerView.computeVerticalScrollRange());
                builder.append("\n当前界面偏移量:");
                builder.append(recyclerView.computeVerticalScrollOffset());
                builder.append("\n当前一屏长度:");
                builder.append(recyclerView.computeVerticalScrollExtent());
                builder.append("\n当前item距离顶部高度:");
                builder.append(currentView.getTop());
                Logger.e(builder.toString());
                getNextChapter();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (data.size()>0){
            bookBean.lastChapterProgress = recyclerView.getChildAt(0).getTop();
            bookBean.lastChapterProgress = Math.abs(bookBean.lastChapterProgress);
            DBCenter.getInstance().updateBook(bookBean);
        }
    }

    private boolean isLoading = false;

    private void startGetContent(final String url) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        Single.just(url)
                .map(new Function<String, ContentResult>() {
                    @Override
                    public ContentResult apply(String url) {
                        return (ContentResult) currentSource.parseContent(url);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ContentResult>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ContentResult o) {
                        data.add(o.data);
                        adapter.notifyDataSetChanged();
                        if (needScrollLastProcess){
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.scrollBy(0, bookBean.lastChapterProgress);
                                }
                            });
                            needScrollLastProcess = false;
                        }
                        // TODO: 2020/7/9 如果当前已经看了很多  则需要把前面的回收掉
                        isLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                    }
                });
    }

    private void getNextChapter() {
        if (recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollOffset() >
                recyclerView.computeVerticalScrollExtent() * 5) {
            return;
        }
        Logger.i("缓存下一页!!!!");
        ChapterBean last = data.get(data.size() - 1);
        startGetContent(last.nextUrl);
        bookBean.lastChapter = last.title;
        bookBean.lastChapterUrl = last.url;
        DBCenter.getInstance().updateBook(bookBean);
    }

    public void onSettingListener(View view) {
        view.setSelected(!view.isSelected());
        if (view.isSelected()) {
            findViewById(R.id.llySetting).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.llySetting).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionMore) {
            if (llyBottom.getVisibility() != View.VISIBLE) {
                llyBottom.setVisibility(View.VISIBLE);
            } else {
                llyBottom.setVisibility(View.GONE);
            }
        }
        return true;
    }

    public void onFontSizeUpListener(View view) {
        int fontSize = AppSharedper.getInstance(this).getInt("fontSize", 12);
        fontSize++;
        AppSharedper.getInstance(this).putInt("fontSize", fontSize);
        tvFontSize.setText(MessageFormat.format("{0}", fontSize));
        adapter.setTextSize(fontSize);
    }

    public void onFontSizeDownListener(View view) {
        int fontSize = AppSharedper.getInstance(this).getInt("fontSize", 12);
        fontSize--;
        AppSharedper.getInstance(this).putInt("fontSize", fontSize);
        tvFontSize.setText(MessageFormat.format("{0}", fontSize));
        adapter.setTextSize(fontSize);
    }

    public void onDayListener(View view) {
        adapter.setNight(false);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.color_EAEDF2));
        tvTitle.setBackgroundColor(getResources().getColor(R.color.color_EAEDF2));
        tvTitle.setTextColor(getResources().getColor(R.color.color_4a4b50));
        AppSharedper.getInstance(this).putBoolean("isNight", false);
    }

    public void onNightListener(View view) {
        adapter.setNight(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.color_171D26));
        tvTitle.setBackgroundColor(getResources().getColor(R.color.color_171D26));
        tvTitle.setTextColor(getResources().getColor(R.color.color_eaedf2));
        AppSharedper.getInstance(this).putBoolean("isNight", true);
    }

    @Override
    public void onTopLeftClick(View view) {
        recyclerView.scrollBy(0, -recyclerView.computeVerticalScrollExtent() * 9 / 10);
    }

    @Override
    public void onTopRightClick(View view) {
        recyclerView.scrollBy(0, -recyclerView.computeVerticalScrollExtent() * 9 / 10);
    }

    @Override
    public void onCenterClick(View view) {
        onSettingListener(view);

    }

    @Override
    public void onBottomLeftClick(View view) {
        recyclerView.scrollBy(0, recyclerView.computeVerticalScrollExtent() - recyclerView.computeVerticalScrollExtent() / 10);
        getNextChapter();
    }

    @Override
    public void onBottomRightClick(View view) {
        recyclerView.scrollBy(0, recyclerView.computeVerticalScrollExtent() - recyclerView.computeVerticalScrollExtent() / 10);
        getNextChapter();
    }
}
