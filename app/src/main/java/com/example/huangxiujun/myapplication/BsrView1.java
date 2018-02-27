package com.example.huangxiujun.myapplication;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.ArgbEvaluator;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WangYu on 2018/2/27.
 */

public class BsrView1 extends View {
    private Context mContext;
    private Paint mPaint, mPointPaint;
    private Path mBezierPath;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private Point startPoint;
    private Point endPoint;
    private Point assistPoint;
    private float startDRate, stopDRate;
    private int mPaintColor;
    private int mStrokeWidth = 6;
    private int mAnimDuration = 3000;
    private int mDelayTime = 1000;


    public BsrView1(Context context) {
        this(context, null);
    }

    public BsrView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BsrView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.BLACK);
        mBezierPath = new Path();
        mPath = new Path();
        startPoint = new Point(300, 600);
        endPoint = new Point(900, 600);
        assistPoint = new Point(600, 300);
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
        mPathMeasure = new PathMeasure();
        mBezierPath.moveTo(startPoint.x, startPoint.y);
        mBezierPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        mPathMeasure.setPath(mBezierPath, false);
        ValueAnimator startDAnimator = ValueAnimator.ofFloat(0, 1);
        startDAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startDRate = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        startDAnimator.setDuration(mAnimDuration);
        startDAnimator.setStartDelay(mDelayTime);
        ValueAnimator stopDAnimator = ValueAnimator.ofFloat(0, 1);
        stopDAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                stopDRate = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        stopDAnimator.setDuration(mAnimDuration);
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), 0xffff0000, 0xff00ff00, 0xff0000ff);
        colorAnimator.setDuration(mAnimDuration);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaintColor = (int) animation.getAnimatedValue();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(startDAnimator).with(stopDAnimator).with(colorAnimator);
        animatorSet.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float stopD = stopDRate * mPathMeasure.getLength();
        float startD = startDRate * mPathMeasure.getLength();
        mPath.reset();
        mPathMeasure.getSegment(startD, stopD, mPath, true);
        mPaint.setColor(mPaintColor);
        canvas.drawPath(mPath, mPaint);
        //end位置的小圆点
        drawPoint(canvas, stopD);
        //start位置的小圆点
        drawPoint(canvas, startD);
    }

    private void drawPoint(Canvas canvas, float D) {
        float x = 0, y = 0, tx = 0, ty = 0;
        float[] pos = {x, y};
        float[] tan = {tx, ty};
        mPathMeasure.getPosTan(D, pos, tan);
        mPointPaint.setColor(mPaintColor);
        canvas.drawCircle(pos[0], pos[1], mStrokeWidth / 2, mPointPaint);
    }

    public class BezierHolder {

    }
}
