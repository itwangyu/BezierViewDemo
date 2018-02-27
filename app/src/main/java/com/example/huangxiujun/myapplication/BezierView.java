package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by WangYu on 2018/2/27.
 */

public class BezierView extends View {
    private Context mContext;

    private int mAnimDuration = 3000;
    private int mDelayTime = 1000;
    private BezierHolder mBezierHolder;
    private BezierHolder mBezierHolder1;


    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mBezierHolder = new BezierHolder(Color.RED,Color.GREEN,Color.BLUE);
        mBezierHolder.setStrokeWidth(10);
        mBezierHolder.setPoints(new Point(300, 600),new Point(600, 300),new Point(900, 600));
        mBezierHolder.setAnimDuration(mAnimDuration,mDelayTime);
        mBezierHolder.setInvalidateListener(new BezierHolder.InvalidateListener() {
            @Override
            public void onInvalidate() {
                postInvalidate();
            }
        });
        mBezierHolder.start();

        mBezierHolder1 = new BezierHolder(Color.RED,Color.GREEN,Color.BLUE);
        mBezierHolder1.setStrokeWidth(10);
        mBezierHolder1.setPoints(new Point(300, 1200),new Point(600, 900),new Point(900, 1200));
        mBezierHolder1.setAnimDuration(mAnimDuration,mDelayTime);
        mBezierHolder1.setInvalidateListener(new BezierHolder.InvalidateListener() {
            @Override
            public void onInvalidate() {
                postInvalidate();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBezierHolder1.start();
            }
        },3000);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBezierHolder.onDraw(canvas);
        mBezierHolder1.onDraw(canvas);
    }

}
