package com.hl.book.util.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by leon
 * 日期： 2018/8/24.
 * 描述：
 */
public class ImageOptionsFactory {

    public static ImageOptions getDefaultOption(Context context, String url, ImageView imageView){
        return new ImageOptions(context,url,imageView);
    }
    public static ImageOptions getCircleOption(Context context, String url, ImageView imageView){
        ImageOptions imageOptions = new ImageOptions(context,url,imageView);
        imageOptions.circle();
        return imageOptions;
    }

    public static ImageOptions getRoundOption(Context context, String url, ImageView imageView, int roundDp){
        ImageOptions imageOptions = new ImageOptions(context,url,imageView);
        imageOptions.round(roundDp);
        return imageOptions;
    }

    public static ImageOptions getAutoOption(Context context, String url, ImageView imageView){
        ImageOptions imageOptions = new ImageOptions(context,url,imageView);
        imageOptions.autoResetIvByRes();
        return imageOptions;
    }
}
