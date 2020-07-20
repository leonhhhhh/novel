package com.hl.book.localdata.database;

import com.hl.book.model.bean.BookBean;

import java.util.ArrayList;
import java.util.List;

public class DBCenter {
    private static DBCenter instance;
    private static DBHelper dbHelper;


    public static synchronized DBCenter getInstance() {
        if (instance == null) {
            instance = new DBCenter();
        }
        return instance;
    }
    private DBCenter(){
        dbHelper = DBHelper.getInstance();
    }

    /**
     * 查询会话列表
     * @return  会话列表
     * */
    public List<BookBean> getBooks(){
        return dbHelper.getBookDao().loadAll();
    }
    public void insertBooks(ArrayList<BookBean> bookBeans){
        if (bookBeans==null)return;
        dbHelper.getBookDao().insertOrReplaceInTx(bookBeans);
    }
    public void insertBook(BookBean book){
        if (book==null)return;
        dbHelper.getBookDao().insertOrReplace(book);
    }
    public void updateBook(BookBean book){
        if (book==null)return;
        dbHelper.getBookDao().update(book);
    }
    public void delBook(BookBean book){
        if (book==null)return;
        dbHelper.getBookDao().delete(book);
    }
}
