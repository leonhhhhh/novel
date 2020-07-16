package com.hl.book.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.hl.book.R;

import java.util.Objects;


/**
 * Created by leon
 * 描述：对话框基类
 */

public class BaseDialog extends Dialog implements View.OnClickListener,DialogInterface.OnKeyListener{
    public Context context;
    private OnDialogListener mOnDialogListener;
    private boolean isClickDismiss = true ;
    protected int layoutId = R.layout.dialog_common ;
    public BaseDialog(Context context) {
        super(context, R.style.base_dialog_style);
        this.context = context;
        iniView();
    }
    public BaseDialog(Context context, int layoutId) {
        super(context, R.style.base_dialog_style);
        this.context = context;
        this.layoutId = layoutId;
        iniView();
    }
    protected void iniView(){
        setContentView(layoutId);
    }
    private int GravityType = Gravity.CENTER ;

    protected void setGravityType(int gravityType) {
        GravityType = gravityType;
    }

    @Override
    public void show() {
        Window window = this.getWindow();
        if (window != null) {
            window.setGravity(GravityType);
        }
        assert window != null;
        window.getAttributes().width = getWindowWidth(context);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        super.show();
    }
    private int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x ;
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }

    /* 触摸外部弹窗 */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isOutOfBounds(getContext(), event)) {
            onTouchOutside();
        }
        return super.onTouchEvent(event);
    }
    private void onTouchOutside(){
        onListener(DialogMessage.RESULT_OUTSIDE);
    }
    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = Objects.requireNonNull(getWindow()).getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }
    public void setOnDialogListener(OnDialogListener mOnDialogListener) {
        this.mOnDialogListener = mOnDialogListener;
    }
    protected void onListener(int result, String message){
        if (mOnDialogListener != null)
            mOnDialogListener.onClick(new DialogMessage(result,message));
    }
    protected void onListener(int result){
        if (mOnDialogListener != null)
            mOnDialogListener.onClick(new DialogMessage(result));
    }
    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
            onListener(DialogMessage.RESULT_BACK);
            return true;
        } else {
            return false;
        }
    }

    public boolean isClickDismiss() {
        return isClickDismiss;
    }

    public void setClickDismiss(boolean clickDismiss) {
        isClickDismiss = clickDismiss;
    }
}
