package com.hl.book.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hl.book.ui.dialog.LoadingDialog;
import com.tencent.mmkv.MMKV;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV.initialize(this);
    }
    LoadingDialog loadingDialog;
    public void showLoading(){
        if (loadingDialog != null && loadingDialog.isShowing()){
            return;
        }
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }
    public void hideLoadingDialog(){
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }
}
