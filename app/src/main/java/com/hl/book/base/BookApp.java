package com.hl.book.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.hl.book.localdata.database.DBHelper;
import com.hl.book.util.crash.Fire;
import com.tencent.mmkv.MMKV;

import java.util.List;

public class BookApp extends Application {
    private static BookApp mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        String processName = getProcessName(android.os.Process.myPid());
        if (getPackageName().equals(processName)) {
            init();
        }
    }
    private void init() {
        iniDB();
        Fire.init(this);
        MMKV.initialize(this);
    }
    public static BookApp getInstance() {
        return mApplication;
    }

    public void iniDB() {
        DBHelper.getInstance().initDB(this);
    }
    public String getProcessName(int pid){
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        assert am != null;
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        if (processInfoList == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
