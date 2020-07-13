package com.hl.book.util.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hl.book.R;

//RequestOptions options = new RequestOptions();
//options.format(DecodeFormat.PREFER_ARGB_8888)
//options.centerCrop()//图片显示类型
//options.placeholder(loadingRes)//加载中图片
//options.error(errorRes)//加载错误的图片
//options.error(new ColorDrawable(Color.RED))//或者是个颜色值
//options.priority(Priority.HIGH)//设置请求优先级
//options.diskCacheStrategy(DiskCacheStrategy.ALL);
//options.diskCacheStrategy(DiskCacheStrategy.RESOURCE)//仅缓存原图片
//options.diskCacheStrategy(DiskCacheStrategy.ALL)//全部缓存
//options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)缓存缩略过的
//options.onlyRetrieveFromCache(true)//仅从缓存加载
//options.skipMemoryCache(true)//禁用缓存,包括内存和磁盘
//options.diskCacheStrategy(DiskCacheStrategy.NONE)仅跳过磁盘缓存
//options.override(200,200)加载固定大小的图片
//options.dontTransform()不做渐入渐出的转换
//options.transition(new DrawableTransitionOptions().dontTransition())//同上
//options.circleCrop()设置成圆形头像&lt;这个是V4.0新增的;
//options.transform(new RoundedCorners(10))设置成圆角头像&lt;这个是V4.0新增的&;

/**
 * Created by leon
 * 日期： 2018/8/24.
 * 描述：
 */
public class ImageOptions {
    private int defaultImg = R.drawable.icon_default_bg;

    private RequestOptions requestOptions ;

    public Context context;
    public String url;
    public ImageView imageView;
    public boolean autoResetIvByRes=false;
    public int maxWidth=0;
    public int maxHeight=0;
    public int reTryCount = 0;
    @SuppressLint("CheckResult")
    public ImageOptions(Context context, String url, ImageView imageView){
        init(context,url,imageView,defaultImg);
        requestOptions.dontAnimate();
    }
    private void init(Context context, String url, ImageView imageView, int defaultImg){
        requestOptions = new RequestOptions().placeholder(defaultImg).error(defaultImg) ;
        this.context = context;
        this.url = url;
        this.imageView = imageView;
    }
    public RequestOptions defaultImg(int defaultImg){
        return requestOptions.placeholder(defaultImg).error(defaultImg);
    }
    public RequestOptions circle(){
        return requestOptions.circleCrop();
    }
    public RequestOptions round(int roundDP){
        int dp = dip2px(context,roundDP) ;
        return requestOptions.dontAnimate().transforms(new RoundedCorners(dp));
    }
    public RequestOptions autoResetIvByRes(){
        autoResetIvByRes = true;
        return requestOptions;
    }

    public RequestOptions getRequestOptions() {
        return requestOptions;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
    public void setMaxSize(int maxWidth,int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public int getDefaultImg() {
        return defaultImg;
    }
}
