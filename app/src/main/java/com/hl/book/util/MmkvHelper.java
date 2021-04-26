package com.hl.book.util;

import com.tencent.mmkv.MMKV;

public class MmkvHelper {
    private static MMKV getApp() {
        return MMKV.mmkvWithID("app");
    }

    private static MMKV getUser() {
        return MMKV.mmkvWithID("user");
    }

    //===========================APP级别=============================================================================
    public static boolean isLogin() {
        return getApp().decodeBool("isLogin", false);
    }

    public static void setLogin(boolean isLogin) {
        getApp().encode("isLogin", isLogin);
    }
    /**
     * 最后登录账号
     */
    public static String getLastLoginMobile() {
        return getApp().decodeString("LastLoginMobile","");
    }
    public static void setLastLoginMobile(String lastLoginMobile) {
        getApp().encode("LastLoginMobile", lastLoginMobile);
    }


    //===========================User级别=============================================================================
    public static void clearUser() {
        getUser().clear();
    }
    /**
     * 搜索历史
     */
    public static String getSearchHistory() {
        return getUser().decodeString("SearchHistory");
    }
    public static void setSearchHistory(String SearchHistory) {
        getUser().encode("SearchHistory", SearchHistory);
    }
}
