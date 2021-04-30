package com.hl.book.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.hl.book.R;
import com.hl.book.base.BaseActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

// TODO: 2020/7/13 个人资料展示
// TODO: 2020/7/28 个人资料修改
// TODO: 2020/7/28 退出功能 
// TODO: 2020/7/28 会员功能 
// TODO: 2020/7/28 设置--应用介绍 
// TODO: 2020/7/28 设置--关于我们 
// TODO: 2020/7/28 设置--客服反馈 
// TODO: 2020/7/28 设置--好评 
public class MeActivity extends BaseActivity {
    private EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setContentView(R.layout.activity_search);
        iniView();
    }

    private void iniView() {
        etSearch = findViewById(R.id.etSearch);
    }
}
