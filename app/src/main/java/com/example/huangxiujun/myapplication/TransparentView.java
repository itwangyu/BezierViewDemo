package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by WangYu on 2018/4/2.
 */
public class TransparentView extends View {
    private View targetView;

    public TransparentView(Context context) {
        super(context);
    }

    public TransparentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TransparentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().getParent().requestDisallowInterceptTouchEvent(true);
        if (targetView != null) {
            targetView.dispatchTouchEvent(event);
        }
        return true;
    }

    public void setDispatchView(View targetView) {
        this.targetView=targetView;
    }
}
