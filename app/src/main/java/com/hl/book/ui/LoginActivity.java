package com.hl.book.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.hl.book.R;
import com.hl.book.base.BaseActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

// TODO: 2020/7/13 微信登录
// TODO: 2020/7/28 qq登录
// TODO: 2020/7/28 手机验证码登录
// TODO: 2020/7/28 支付功能
// TODO: 2020/7/28 广告接入
public class LoginActivity extends BaseActivity {
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
