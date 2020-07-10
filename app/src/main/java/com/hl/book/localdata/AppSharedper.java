package com.hl.book.localdata;

import android.app.Application;
import android.content.Context;


/**
 * 描述:全局Sharedper
 * 作者:Leon
 * 时间:2016/12/22 0022
 */

public class AppSharedper extends BaseSharedper {
    private static AppSharedper instance =null;

    private AppSharedper(Context context) {
        super(context, "Leon_App" );
    }
    public static AppSharedper getInstance(Context context) {
        if (instance==null){
            instance = new AppSharedper(context);
        }
        return instance;
    }
}
