package com.hl.book.localdata.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hl.book.dao.BookBeanDao;
import com.hl.book.dao.DaoMaster;
import com.hl.book.dao.DaoSession;
import com.orhanobut.logger.Logger;


public class DBHelper {
    @SuppressLint("StaticFieldLeak")
    private static DBHelper instance;

    private BookBeanDao bookDao;
    private Context context;
    private DaoSession daoSession;

    public static synchronized DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    // TODO: 2020/7/20 按登录用户设置数据库名
    public void initDB(Context context) {
        this.context = context;
        initGitlabDB("book");
    }

    private void initGitlabDB(String dbName) {
        daoSession = createSession(context, dbName);
        bookDao = daoSession.getBookBeanDao();
    }

    private DaoSession createSession(Context context, String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }


    public BookBeanDao getBookDao() {
        if (bookDao == null) {
            Logger.e("数据库未初始化");
        }
        return bookDao;
    }

    public void clear() {
        daoSession.clear();
    }
}
