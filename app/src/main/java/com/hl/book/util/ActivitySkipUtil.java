package com.hl.book.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.Serializable;


/**
 * 描述:应用界面跳转帮助类
 * 作者:Leon
 * 时间:2017/11/27 0027
 */

public class ActivitySkipUtil {
    //可跳转外链的
    public static void jumpByType(Context context, String type, int id, String url){
        if(type.equals("1")) {  //客户端内部跳转
            jumpById(context,id);
        } else if(type.equals("3")) {   //外链跳转
            jumpByUrl(context,url);
        }
    }
    //通过Id固定跳转
    public static void jumpById(Context context, int id){
        switch (id){
            case 1000:
                break;

        }
    }
    private static void skipAct(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
    public static void skipAct(Context context, Class<?> cls, Object...data){
        if (context==null)return;
        Intent intent = new Intent(context, cls);
        for (int i = 0; i <data.length ; i=i+2) {
            if (data[i+1] instanceof Integer){
                intent.putExtra(StrUtil.nullToStr(data[i]),StrUtil.nullToIntf1(data[i+1])) ;
            }else if (data[i+1] instanceof Serializable){
                intent.putExtra(StrUtil.nullToStr(data[i]),(Serializable)data[i+1]) ;
            }else if (data[i+1] instanceof Boolean){
                intent.putExtra(StrUtil.nullToStr(data[i]),StrUtil.nullToBoolean(data[i+1])) ;
            }else {
                intent.putExtra(StrUtil.nullToStr(data[i]),StrUtil.nullToStr(data[i+1])) ;
            }
        }
        context.startActivity(intent);
    }


    //跳转外链
    public static void jumpByUrl(Context context, String url) { //跳转h5外链
        Intent intent=new Intent();//创建Intent对象
        intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
        intent.setData(Uri.parse(url));//为Intent设置数据
        context.startActivity(intent);
    }


    //带有参数的跳转
    public static void jumpDetailPage(Context context, int id, String detailId) {
        switch (id) {
            case 1006:
                break;

        }
    }
    public static void openAppMain(Context context, String packageName){
        PackageManager manager = context.getPackageManager();
        Intent app = manager.getLaunchIntentForPackage(packageName);
        context.startActivity(app);
    }



}
