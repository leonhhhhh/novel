package com.hl.book.listener;

import android.view.View;

public interface ReadClickListener {
    void onTopLeftClick(View view);
    void onTopRightClick(View view);
    void onCenterClick(View view);
    void onBottomLeftClick(View view);
    void onBottomRightClick(View view);
}
