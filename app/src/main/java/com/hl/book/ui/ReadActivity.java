package com.hl.book.ui;

import android.annotation.SuppressLint;
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
import com.hl.book.listener.ReadClickListener;
import com.hl.book.localdata.AppSharedper;
import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.model.bean.TextBean;
import com.hl.book.source.SourceManager;
import com.hl.book.source.download.DownloadManager;
import com.hl.book.source.result.ContentResult;
import com.hl.book.source.source.Source;
import com.hl.book.ui.adapter.ReadAdapter;
import com.hl.book.ui.view.ReadClickView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// TODO: 2021/2/15 下载小说
public class ReadActivity extends AppCompatActivity implements ReadClickListener {
    private static final String TAG = "ReadActivity";
    private ReadAdapter adapter;
    private List<ChapterBean> data;
    private List<ChapterBean> allChapterList;
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
        int fontSize = AppSharedper.getInstance(this).getInt("fontSize", 16);
        tvFontSize.setText(MessageFormat.format("{0}", fontSize));
        iniListener();
        data = new ArrayList<>();
        allChapterList = DBCenter.getInstance().getChapterListByBook(bookBean.url);

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
        TextBean textBean = DBCenter.getInstance().getChapterContentByUrl(chapterBean.url);
        if (textBean != null){
            chapterBean.textBean = textBean;
            data.add(chapterBean);
            adapter.notifyDataSetChanged();
        }else {
            startGetContent(chapterBean);
        }
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
//                builder.delete(0,builder.length());
//                builder.append("\n当前item:");
//                builder.append(currentPosition);
//                builder.append("\n当前界面总长度:");
//                builder.append(recyclerView.computeVerticalScrollRange());
//                builder.append("\n当前界面偏移量:");
//                builder.append(recyclerView.computeVerticalScrollOffset());
//                builder.append("\n当前一屏长度:");
//                builder.append(recyclerView.computeVerticalScrollExtent());
//                builder.append("\n当前item距离顶部高度:");
//                builder.append(currentView.getTop());
//                Logger.e(builder.toString());
                cleanDataIfTooMuch(currentPosition);
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

    private void startGetContent(ChapterBean chapterBean) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        Single.just(chapterBean)
                .map(new Function<ChapterBean, ContentResult>() {
                    @Override
                    public ContentResult apply(ChapterBean chapterBean1) {
                        return (ContentResult) currentSource.parseContent(chapterBean1);
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
                        scrollLastProcess();
                        isLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                    }
                });
    }
    private void scrollLastProcess(){
        if (needScrollLastProcess){
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollBy(0, bookBean.lastChapterProgress);
                }
            });
            needScrollLastProcess = false;
        }
    }
    private void getNextChapter() {
        if (recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollOffset() >
                recyclerView.computeVerticalScrollExtent() * 5) {
            return;
        }
        if (data.size() >= allChapterList.size()|| data.size()==0){
            return;
        }
        //Logger.i("缓存下一页!!!!");
        ChapterBean lastBean = data.get(data.size()-1);
        if (lastBean.index+1>=allChapterList.size()){
            return;
        }
        ChapterBean next = allChapterList.get(lastBean.index+1);
        TextBean textBean = DBCenter.getInstance().getChapterContentByUrl(next.url);
        if (textBean == null){
            startGetContent(next);
        }else {
            next.textBean = textBean;
            next.title = next.title+"(本地缓存)";
            data.add(next);
            adapter.notifyDataSetChanged();
            scrollLastProcess();
        }
        bookBean.lastChapter = next.title;
        bookBean.lastChapterUrl = next.url;
        DBCenter.getInstance().updateBook(bookBean);
    }

    /**
     * @param currentPosition  看了超过15个  回收掉前面五个
     */
    private void cleanDataIfTooMuch(int currentPosition){
        if (adapter.getItemCount()>15 && adapter.getItemCount()-currentPosition<3){
            data.subList(0, 5).clear();
            adapter.notifyItemRangeRemoved(0,5);
        }

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

    /**
     * @param view 下载一部分(默认30章) 尝鲜版
     */
    public void onDownloadPartListener(View view) {
        int MAX_COUNT = 30;
        onDownloadAction(MAX_COUNT);
    }

    /**
     * @param view 下载全文
     */
    public void onDownloadListener(View view) {
        onDownloadAction(allChapterList.size());
    }
    private void onDownloadAction(int maxCount){
        if (data.size()<=0){
            return;
        }
        int count = 0;
        ChapterBean bean = data.get(data.size()-1);
        for (int i = 0; i < allChapterList.size(); i++) {
            if (count== maxCount){
                break;
            }
            if (count>0){
                Log.e(TAG,"添加下载:"+allChapterList.get(i).title);
                DownloadManager.getInstance().download(allChapterList.get(i));
                count++;
                continue;
            }
            if (allChapterList.get(i).url.equals(bean.url)){
                DownloadManager.getInstance().download(allChapterList.get(i));
                count++;
            }
        }
    }
}
