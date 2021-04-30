package com.hl.book.ui.dialog;

import android.content.Context;
import android.view.View;

import com.hl.book.R;
import com.hl.book.ui.dialog.base.BaseDialog;


/**
 * Created by leon
 * 描述：加载对话框
 */

public class LoadingDialog extends BaseDialog {

    public LoadingDialog(Context context){
        super(context, R.layout.dialog_loading);
    }

    @Override
    protected void iniView() {
        super.iniView();
        setContentView(layoutId);
    }

    @Override
    public void onClick(View v) {
    }
}
