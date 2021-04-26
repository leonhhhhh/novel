package com.hl.book.util.crash;

import android.app.Application;

import com.hl.book.BuildConfig;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

/**
 * 使用统计  错误上报
 */
public class Fire {
    public static void init(Application application){
        if (BuildConfig.DEBUG){
            AppCenter.start(application, "1d7ab460-8c6d-40cc-a1b0-8601184bd14b",
                    Analytics.class, Crashes.class);
        }else {
            AppCenter.start(application, "fbabdb75-8dd1-4b89-999d-cc4e9edb0d81",
                    Analytics.class, Crashes.class);
        }
    }
}
