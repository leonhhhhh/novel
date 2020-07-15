package com.hl.book.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.hl.book.listener.ReadClickListener;

public class ReadClickView extends RelativeLayout {
    private float downX ;
    private float downY ;
    private boolean isClick = true;
    private ReadClickListener clickListener;
    private boolean isClickAble = true;
    public ReadClickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN){
            downX = ev.getX();
            downY = ev.getY();
            isClick = true;
        }else if (ev.getAction()==MotionEvent.ACTION_MOVE){
            if (isClick&&(Math.abs(downX-ev.getX())>20||Math.abs(downY-ev.getY())>20)){
                isClick = false;
            }
        }else if (ev.getAction()==MotionEvent.ACTION_UP){
            if (Math.abs(downX-ev.getX())>20||Math.abs(downY-ev.getY())>20){
                isClick = false;
            }
            if (isClick&&(ev.getEventTime()-ev.getDownTime()<300)){
                callClickListener((int) downX,(int) downY);
            }
        }else {
            isClick = false;
        }
        return super.dispatchTouchEvent(ev);
    }
    private void callClickListener(int downX,int downY){
        if (clickListener==null)return;
        if (!isClickAble)return;
        int width = getWidth();
        int height = getHeight();
        if (downY<height/3){
            if (downX<width/2){
                clickListener.onTopLeftClick(this);
            }else {
                clickListener.onTopRightClick(this);
            }
        }else if (downY<height*2/3){
            clickListener.onCenterClick(this);
        }else {
            if (downX<width/2){
                clickListener.onBottomLeftClick(this);
            }else {
                clickListener.onBottomRightClick(this);
            }
        }
    }
    public void setClickListener(ReadClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setClickAble(boolean clickAble) {
        isClickAble = clickAble;
    }
}
