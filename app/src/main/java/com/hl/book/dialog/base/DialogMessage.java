package com.hl.book.dialog.base;

/**
 * Created by leon
 * 描述：对话框回调包
 */

public class DialogMessage {
    /** 确认1*/
    public static final int RESULT_OK = 1 ;
    /** 取消2*/
    public static final int RESULT_CANCEL = 2 ;
    /** 外部透明3*/
    public static final int RESULT_OUTSIDE = 3 ;
    /** 返回键4*/
    public static final int RESULT_BACK = 4 ;
    private int result ;
    private String message ;
    public DialogMessage(int result, String message){
        this.result = result ;
        this.message = message ;
    }
    public DialogMessage(int result){
        this.result = result ;
        this.message = "" ;
    }
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
