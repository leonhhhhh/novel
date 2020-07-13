package com.hl.book.util.image;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.bumptech.glide.Glide;
import com.hl.book.util.StrUtil;

/**
 * 描述:图片加载类
 * 作者:LeonF
 */

public class ImageLoadUtil{
    /**
     * 加载图片统一入口
     */
    @SuppressLint("CheckResult")
    public static void loadImg(final ImageOptions options) {
        if (options.context==null)return;
        try {
            if (options.context instanceof Activity){
                Activity activity = (Activity)options.context ;
                if (activity.isFinishing())return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (StrUtil.isEmpty(options.url)){
            options.imageView.setImageResource(options.getDefaultImg());
            return;
        }
        Glide.with(options.context).load(options.url).apply(options.getRequestOptions()).into(options.imageView);
    }
}
