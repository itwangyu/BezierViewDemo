package com.example.huangxiujun.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WangYu on 2018/4/18.
 */
public class ScanView extends View {
    private Context mContext;
    private int mWidth;
    private int mHight;
    private RectF rectF;
    private Paint mPaint;
    private float mStartAngle,mEndAngle;
    private ValueAnimator mStartAngleAnimator;
    private ValueAnimator mEndAngleAnimator;
    private AnimatorSet mAnimatorSet;

    private static final int VALUE_ANIMATION_DURATION=1000,VALUE_START_DELAY_TIME=150;
    private static final float VALUE_FLOAT_START_ANGLE=-90;

    public ScanView(Context context) {
        this(context,null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init();

    }


    private void init() {
     resetAngle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0x66ff0000);
        mStartAngleAnimator = ValueAnimator.ofFloat(VALUE_FLOAT_START_ANGLE, VALUE_FLOAT_START_ANGLE+360);
        mStartAngleAnimator.setDuration(VALUE_ANIMATION_DURATION);
        mStartAngleAnimator.setStartDelay(VALUE_START_DELAY_TIME);
        mStartAngleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartAngle= (float) animation.getAnimatedValue();
            }
        });
        mEndAngleAnimator = ValueAnimator.ofFloat(VALUE_FLOAT_START_ANGLE, VALUE_FLOAT_START_ANGLE+360);
        mEndAngleAnimator.setDuration(VALUE_ANIMATION_DURATION);
        mEndAngleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mEndAngle = (float) animation.getAnimatedValue();
            }
        });
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(mStartAngleAnimator).with(mEndAngleAnimator);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                resetAngle();
            }
        });
    }

    private void resetAngle() {
        mStartAngle = VALUE_FLOAT_START_ANGLE;
        mEndAngle=VALUE_FLOAT_START_ANGLE;
    }

    public void start() {
        mAnimatorSet.start();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF == null) {
            rectF = new RectF(0, 0, mWidth, mHight);
        }
        canvas.drawArc(rectF,mStartAngle,mEndAngle-mStartAngle,true,mPaint);
        if (mStartAngle < VALUE_FLOAT_START_ANGLE+360 || mEndAngle < VALUE_FLOAT_START_ANGLE+360) {
            postInvalidate();
        }
    }
}
