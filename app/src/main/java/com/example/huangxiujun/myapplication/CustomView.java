package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYu on 2018/1/19.
 */

public class CustomView extends View {
    private Context mContext;
    private Paint mMainViewPaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private int mWidth;
    private int mHeight;
    private int baseline;
    //里面按钮部分
    private int mBigRadius;//大圆半径
    private int mStrokeWidth = 4;//画笔宽度
    private int mLineHeight = 10;//中间竖线长度（dp）
    private double mSmallRatio = 0.8;//点击的时候缩小的倍数
    private double mBigCirRatio = 0.5;//外圈园是整个view高度的多少
    private double mSmallCirRatio = 0.4;//里面小圆是大圆的多少
    private int mSmallAngle=30;

    //外面动画部分
    private  Paint mActivePaint;//
    /** 是否正在扩散中 */
    private boolean mIsWoring = true;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();

    private static String TAG = "wangyu";
    private RectF mRectf;
    private final int PRESS_STATE_DOWN = 0, PRESS_STATE_UP = 1, PRESS_STATE_STOP = 2;
    private int mPressState = PRESS_STATE_STOP;
    private int centerX;
    private int centerY;
    private final int STATE_START=6,STATE_STOP=7;
    private int mCurrentState=STATE_STOP;
    private String text;
    private Rect bounds;

    public CustomView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        //外圈圆的半径
        mBigRadius = (int) (mHeight * mBigCirRatio / 2);
        Log.i(TAG, "onMeasure: " + mBigCirRatio + "  " + mHeight);
    }

    private void init() {
        mMainViewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMainViewPaint.setColor(Color.BLACK);
        mMainViewPaint.setStrokeWidth(mStrokeWidth);
        mMainViewPaint.setStyle(Paint.Style.STROKE);

        mActivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mActivePaint.setColor(Color.RED);
        mActivePaint.setStrokeWidth(mStrokeWidth);
        mActivePaint.setStyle(Paint.Style.STROKE);
        mAlphas.add(200);
        mWidths.add(0);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        text = "STOP";
        mTextPaint.setTextSize(60);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        baseline = (mWidth - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆心  mWidth/2,mHeight/2
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        //画大圆
        canvas.drawCircle(centerX, centerY, mBigRadius, mMainViewPaint);

        if (mCurrentState == STATE_START) {
            //里圈圆半径
            int mSmallRadius = (int) (mBigRadius * mSmallCirRatio);
            mRectf = new RectF(centerX - mSmallRadius, centerY - mSmallRadius, centerX + mSmallRadius, centerY + mSmallRadius);
            canvas.drawArc(mRectf, -90 + mSmallAngle, 300, false, mMainViewPaint);
            //画竖线
            float y = (float) (Math.cos(Math.PI * mSmallAngle / 180) * mSmallRadius);
            canvas.drawLine(centerX, centerY - y - dip2px(mContext, mLineHeight),
                    centerX, centerY - y + dip2px(mContext, mLineHeight), mMainViewPaint);
        } else {
            canvas.drawText(text,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mTextPaint);
        }

        //画扩散园
        drawActiveCircle(canvas);
        if (mPressState == PRESS_STATE_DOWN) {
            //按下
            if (mBigRadius >= mHeight * mBigCirRatio / 2 * mSmallRatio) {
                mBigRadius -= 2;
            }
        } else if (mPressState == PRESS_STATE_UP) {
            //松开
            if (mBigRadius < mHeight * mBigCirRatio / 2) {
                mBigRadius += 2;
            }
        }
        if (mIsWoring) {
            postInvalidate();
        } else {
            if (mPressState == PRESS_STATE_DOWN || mPressState == PRESS_STATE_UP) {
                postInvalidate();
            }
        }

    }


    /**
     * 画扩散圆
     * @param canvas
     */
    private void drawActiveCircle(Canvas canvas) {
        for (int i = 0; i < mAlphas.size(); i++) {
            // 设置透明度
            Integer alpha = mAlphas.get(i);
            mActivePaint.setAlpha(alpha);
            // 绘制扩散圆
            Integer width = mWidths.get(i);
            canvas.drawCircle(centerX, centerY, (int) (mHeight * mBigCirRatio / 2) + width, mActivePaint);

                alpha-=2;
                if (alpha < 0) {
                    alpha=0;
                }
                mAlphas.set(i, alpha);
                mWidths.set(i, width + 1);
        }
        // 判断当扩散圆扩散到指定宽度时添加新扩散圆
        if (mWidths.get(mWidths.size() - 1) == mHeight*mBigCirRatio/2 / 3) {
            mAlphas.add(200);
            mWidths.add(0);
        }
        if (mWidths.get(0)>mHeight*(1-mBigCirRatio)/2-mStrokeWidth/2) {
            //如果园达到边界，移除他
            mWidths.remove(0);
            mAlphas.remove(0);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPressState = PRESS_STATE_DOWN;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPressState = PRESS_STATE_UP;
                postInvalidate();
                break;
        }
        return true;
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
