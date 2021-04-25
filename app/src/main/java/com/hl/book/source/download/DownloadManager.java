package com.hl.book.source.download;

import com.hl.book.localdata.database.DBCenter;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.model.bean.TextBean;
import com.orhanobut.logger.Logger;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * v1下载器
 */
public class DownloadManager {
    private static DownloadManager instance;

    private static final int downloadMaxCount = 10;
    private CopyOnWriteArrayList<DownloadPackage> currentDownloadList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<DownloadPackage> preDownloadList = new CopyOnWriteArrayList<>();
    private final Object finishLock = new Object();
    public static DownloadManager getInstance() {
        if (instance == null){
            instance = new DownloadManager();
        }
        return instance;
    }

    private DownloadManager() {
    }
    public void download(ChapterBean chapterBean){
        DownloadPackage downloadPackage = new DownloadPackage(chapterBean);
        if (!downloadPackage.check()){
            Logger.e("下载初始化出错: "+chapterBean.title);
            return;
        }
        if (currentDownloadList.size()<downloadMaxCount){
            Logger.v("添加下载队列: "+chapterBean.title);
            currentDownloadList.add(downloadPackage);
            downloadAction(downloadPackage);
        }else {
            Logger.v("添加预下载: "+"chapterBean.title");
            preDownloadList.add(downloadPackage);
        }
    }
    private void downloadAction(final DownloadPackage downloadPackage){
        Single.just(downloadPackage)
                .delay(new Random().nextInt(100), TimeUnit.MILLISECONDS)
                .map(new Function<DownloadPackage, DownloadPackage>() {
                    @Override
                    public DownloadPackage apply(DownloadPackage downloadPackage) {
                        downloadPackage.source.parseContent(downloadPackage);
                        return downloadPackage;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DownloadPackage>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DownloadPackage o) {
                        ChapterBean bean = o.chapterBean;
                        //更新章节表has-download to 1
                        bean.hasDownloaded = ChapterBean.DOWNLOADED;
                        DBCenter.getInstance().updateChapter(bean);
                        //插入章节内容表
                        TextBean textBean = new TextBean(bean.url,bean.textBean.content);
                        DBCenter.getInstance().insertTextBean(textBean);
                        //移除当前下载
                        downloadFinish(o);
                        String content  =textBean.content.length()>10?textBean.content.substring(0,10):textBean.content;
                        Logger.v("下载完成: "+bean.title+content);
                    }

                    @Override
                    public void onError(Throwable e) {
                        downloadFinish(downloadPackage);
                        Logger.v("下载出错: "+downloadPackage.chapterBean.title+e.getMessage());
                    }
                });
    }
    private void downloadFinish(DownloadPackage downloadPackage){
        synchronized (finishLock){
            currentDownloadList.remove(downloadPackage);
            if (preDownloadList.size()>0){
                DownloadPackage currentPackage = preDownloadList.get(0);
                preDownloadList.remove(currentPackage);
                Logger.v("转下载队列: "+currentPackage.chapterBean.title);
                currentDownloadList.add(currentPackage);
                downloadAction(currentPackage);
            }
        }
    }
}
