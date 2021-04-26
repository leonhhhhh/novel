package com.hl.book.localdata.database;

import com.hl.book.dao.BookBeanDao;
import com.hl.book.dao.ChapterBeanDao;
import com.hl.book.dao.TextBeanDao;
import com.hl.book.model.bean.BookBean;
import com.hl.book.model.bean.ChapterBean;
import com.hl.book.model.bean.TextBean;

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
     * @param textBean 插入单个章节内容
     */
    public void insertTextBean(TextBean textBean){
        if (textBean==null)return;
        dbHelper.getTextDao().insertOrReplace(textBean);
    }
    /**
     * @param url 查询单个章节内容
     * @return 章节内容
     */
    public TextBean getChapterContentByUrl(String url){
        return dbHelper.getTextDao().queryBuilder().where(TextBeanDao.Properties.TextId.eq(url)).unique();
    }
    /**
     * 查询章节列表
     * */
    public List<ChapterBean> getChapterListByBook(String bookId){
        return dbHelper.getChapterDao().queryBuilder().orderAsc(ChapterBeanDao.Properties.Index).
                where(ChapterBeanDao.Properties.BookId.eq(bookId)).list();
    }

    /**
     * 插入章节列表
     */
    public void insertChapters(List<ChapterBean> chapterBeans){
        if (chapterBeans==null)return;
        dbHelper.getChapterDao().insertOrReplaceInTx(chapterBeans);
    }

    /**
     * @param url 通过章节完整链接查询对应章节
     * @return
     */
    public ChapterBean getChapterByChapterUrl(String url){
        return dbHelper.getChapterDao().queryBuilder().where(ChapterBeanDao.Properties.Url.eq(url)).unique();
    }
    /**
     * @param chapterBean 更新单个章节
     */
    public void updateChapter(ChapterBean chapterBean){
        if (chapterBean==null)return;
        dbHelper.getChapterDao().update(chapterBean);
    }
    /**
     * 查询书架列表
     * */
    public List<BookBean> getBooks(){
        return dbHelper.getBookDao().loadAll();
    }
    public BookBean getBookByUrl(String bookUrl){
        return dbHelper.getBookDao().queryBuilder().where(BookBeanDao.Properties.Url.eq(bookUrl)).unique();
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
