package com.hl.book.localdata.database;

import com.hl.book.dao.ChapterBeanDao;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;

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
     * 查询章节列表
     * */
    public List<ChapterBean> getChapterListByBook(String bookId){
        return dbHelper.getChapterDao().queryBuilder().where(ChapterBeanDao.Properties.BookId.eq(bookId)).list();
    }

    /**
     * 插入章节列表
     */
    public void insertChapters(List<ChapterBean> chapterBeans){
        if (chapterBeans==null)return;
        dbHelper.getChapterDao().insertOrReplaceInTx(chapterBeans);
    }
    public ChapterBean getChapterByChapterUrl(String url){
        return dbHelper.getChapterDao().queryBuilder().where(ChapterBeanDao.Properties.Url.eq(url)).unique();
    }
    /**
     * 查询书架列表
     * */
    public List<BookBean> getBooks(){
        return dbHelper.getBookDao().loadAll();
    }
    public BookBean getBook(String key){
        return dbHelper.getBookDao().load(key);
    }
    public void insertBooks(ArrayList<BookBean> bookBeans){
        if (bookBeans==null)return;
        dbHelper.getBookDao().insertOrReplaceInTx(bookBeans);
    }

    /**
     * @param book 插入单个书籍
     */
    public void insertBook(BookBean book){
        if (book==null)return;
        dbHelper.getBookDao().insertOrReplace(book);
    }

    /**
     * @param book 更新单个书籍
     */
    public void updateBook(BookBean book){
        if (book==null)return;
        dbHelper.getBookDao().update(book);
    }

    /**
     * @param book 删除单个书籍
     */
    public void delBook(BookBean book){
        if (book==null)return;
        dbHelper.getBookDao().delete(book);
    }
}
