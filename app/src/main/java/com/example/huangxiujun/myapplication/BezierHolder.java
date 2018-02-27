package com.example.huangxiujun.myapplication;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.graphics.drawable.ArgbEvaluator;

/**
 * Created by WangYu on 2018/2/27.
 */

public class BezierHolder {
    private Paint mPaint, mPointPaint;
    private Path mBezierPath;
    private Path mTruncationPath;
    private PathMeasure mPathMeasure;
    private float startDRate, stopDRate;
    private int mPaintColor=Color.BLACK;
    private int mStrokeWidth;
    private ValueAnimator mStartDAnimator;
    private ValueAnimator mStopDAnimator;
    private ValueAnimator mColorAnimator;
    private AnimatorSet mAnimatorSet;
    private InvalidateListener mInvalidateListener;
    private boolean mShouldDrawPoint=false;

    public BezierHolder(Object... paintColors) {
        //init paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setDither(true);
        mPointPaint.setColor(Color.BLACK);
        //init path
        mBezierPath = new Path();
        mTruncationPath = new Path();
        //init pathmeasure
        mPathMeasure = new PathMeasure();
        //init Animtor
        mStartDAnimator = ValueAnimator.ofFloat(0, 1);
        mStartDAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startDRate = (float) animation.getAnimatedValue();
                if (mInvalidateListener != null) {
                    mInvalidateListener.onInvalidate();
                }
                if (startDRate >= 1) {
                    mShouldDrawPoint=false;
                }
            }
        });
        mStopDAnimator = ValueAnimator.ofFloat(0, 1);
        mStopDAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                stopDRate = (float) animation.getAnimatedValue();
                if (mInvalidateListener != null) {
                    mInvalidateListener.onInvalidate();
                }
            }
        });
        mColorAnimator = ValueAnimator.ofObject(ArgbEvaluator.getInstance(), paintColors);
        mColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaintColor = (int) animation.getAnimatedValue();
            }
        });
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(mStartDAnimator).with(mStopDAnimator).with(mColorAnimator);
    }

    public void setStrokeWidth(int strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        mPaint.setStrokeWidth(strokeWidth);
    }

    public void setPoints(Point startPoint, Point assistPoint, Point endPoint) {
        //set point
        mBezierPath.moveTo(startPoint.x, startPoint.y);
        mBezierPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        mPathMeasure.setPath(mBezierPath, false);
    }

    public void setAnimDuration(int duration, int delayTime) {
        mStartDAnimator.setDuration(duration);
        mStartDAnimator.setStartDelay(delayTime);
        mStopDAnimator.setDuration(duration);
        mColorAnimator.setDuration(duration+delayTime);
    }

    public void start() {
        mShouldDrawPoint=true;
        mAnimatorSet.start();
    }

    public void setInvalidateListener(InvalidateListener listener) {
        this.mInvalidateListener = listener;
    }

    public void onDraw(Canvas canvas) {
        float stopD = stopDRate * mPathMeasure.getLength();
        float startD = startDRate * mPathMeasure.getLength();
        mTruncationPath.reset();
        mPathMeasure.getSegment(startD, stopD, mTruncationPath, true);
        mPaint.setColor(mPaintColor);

        canvas.drawPath(mTruncationPath, mPaint);
        if (mShouldDrawPoint) {
            //end位置的小圆点
            drawPoint(canvas, stopD);
            //start位置的小圆点
            drawPoint(canvas, startD);
        }
    }

    private void drawPoint(Canvas canvas, float D) {
        float x = 0, y = 0, tx = 0, ty = 0;
        float[] pos = {x, y};
        float[] tan = {tx, ty};
        mPathMeasure.getPosTan(D, pos, tan);
        mPointPaint.setColor(mPaintColor);
        canvas.drawCircle(pos[0], pos[1], mStrokeWidth / 2, mPointPaint);
    }


    public interface InvalidateListener {
        void onInvalidate();
    }
}
