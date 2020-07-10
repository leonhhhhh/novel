package com.hl.book.localdata;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 描述:Sharedper基类
 * 作者:Leon
 * 时间:2016/12/22 0022
 */

public class BaseSharedper {
    private SharedPreferences share;

    public BaseSharedper(Context context, String name) {

        share = context.getSharedPreferences(name, Context.MODE_PRIVATE);

    }

    public String getString(String key, String defValue) {
        return share.getString(key, defValue);
    }

    public Boolean getBoolean(String key, boolean defValue) {
        return share.getBoolean(key, defValue);

    }

    public int getInt(String key, int defValue) {
        return share.getInt(key, defValue);
    }
    public long getLong(String key, long defValue) {
        return share.getLong(key, defValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = share.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = share.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    /**
     * 清空某个Key的数据
     * @param key
     * @return
     */
    public void clearInfo(String key) {
        SharedPreferences.Editor editor = share.edit();
        editor.remove(key);
        editor.commit();
    }
    /**
     * 清空全部数据
     * @return
     */
    public void clearAllInfo() {
        SharedPreferences.Editor editor = share.edit();
        editor.clear();
        editor.commit();
    }
}
