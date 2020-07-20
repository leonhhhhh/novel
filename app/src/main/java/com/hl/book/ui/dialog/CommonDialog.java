package com.hl.book.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.ui.dialog.base.BaseDialog;
import com.hl.book.ui.dialog.base.DialogMessage;


/**
 * Created by leon
 * 描述：通用对话框
 */

public class CommonDialog extends BaseDialog {
    private TextView mDoneTv;
    private TextView mNameTv, mContentTv;

    public CommonDialog(Context context, int layoutId) {
        super(context, layoutId);
    }
    public CommonDialog(Context context){
        super(context);
    }
    @Override
    protected void iniView() {
        super.iniView();
        setContentView(layoutId);
        mDoneTv = findViewById(R.id.tv_done);
        mNameTv = findViewById(R.id.tv_name);
        mContentTv = findViewById(R.id.tv_content);
        mDoneTv.setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_done) {
            onListener(DialogMessage.RESULT_OK);
        } else if (v.getId() == R.id.tv_cancel) {
            onListener(DialogMessage.RESULT_CANCEL);
        }
        dismiss();
    }

    public TextView getDoneTv() {
        return mDoneTv;
    }

    public TextView getContentTv() {
        return mContentTv;
    }

}
