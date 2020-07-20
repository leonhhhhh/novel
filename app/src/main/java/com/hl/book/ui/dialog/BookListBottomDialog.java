package com.hl.book.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hl.book.R;
import com.hl.book.ui.dialog.base.BaseDialog;
import com.hl.book.ui.dialog.base.DialogMessage;


/**
 * Created by leon
 * 描述：书籍列表设置底部对话框
 */

public class BookListBottomDialog extends BaseDialog {

    public BookListBottomDialog(Context context){
        super(context,R.layout.dialog_book_list_bottom);
    }
    @Override
    protected void iniView() {
        super.iniView();
        TextView tvDel = findViewById(R.id.tvDel);
        TextView tvBookDetail = findViewById(R.id.tvBookDetail);
        tvDel.setOnClickListener(this);
        tvBookDetail.setOnClickListener(this);
        setGravityType(Gravity.BOTTOM);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDel) {
            onListener(DialogMessage.RESULT_OK,"del");
        } else if (v.getId() == R.id.tvBookDetail) {
            onListener(DialogMessage.RESULT_OK,"detail");
        }
        dismiss();
    }
}
