package com.example.huangxiujun.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 对勾动画
 * Created by wangyu on 2018/1/20.
 */

public class TickView extends View {
    private Context context;
    private Paint paint;
    private int width, height;
    private float circleAniValue, successAniValue;
    private Path successPath;
    private PathMeasure pathMeasure;
    private Path mPath;//截取到的真正用来画图的path
    private Paint mCirclePaint;
    private TypedArray mTypedArray;
    //配置参数
    private int mDuration = 1000;
    private float mStrokeWidth = 10;
    private float mBigCirRatio = 0.8f;//外圈园是整个view高度的多少
    private int mColor = Color.WHITE;
    private AnimatorSet animatorSet;
    private boolean mIsWorking = false;
    private OnAnimEndListener animEndListener;
    private float mStartAngle = 0;
    private float mSweepAngle = 0;


    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TickView, defStyleAttr, 0);
        initPara();
    }

    /**
     * 初始化一些参数
     */
    private void initPara() {
        if (mTypedArray != null) {
            mDuration = mTypedArray.getInt(R.styleable.TickView_duration, mDuration);
            mStrokeWidth = mTypedArray.getDimension(R.styleable.TickView_tickStrokeWith, mStrokeWidth);
            mColor = mTypedArray.getColor(R.styleable.TickView_tickColor, mColor);
            mBigCirRatio = mTypedArray.getFloat(R.styleable.TickView_tickCirRatio, mBigCirRatio);
        }

        //画笔
        paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        //圆点画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mColor);
        // 外圈圆取值
        final ValueAnimator cirvleAnimator = ValueAnimator.ofFloat(0, 1);
        cirvleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAniValue = (float) animation.getAnimatedValue();
                mSweepAngle=360*circleAniValue;
                invalidate();
            }
        });
        ValueAnimator succrssAnimator = ValueAnimator.ofFloat(0, 1);
        succrssAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                successAniValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        successPath = new Path();
        mPath = new Path();
        pathMeasure = new PathMeasure();

        animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.play(cirvleAnimator).before(succrssAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (animEndListener != null) {
                    animEndListener.onAnimEnd();
                }
            }

        });
//        animatorSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsWorking) {
            return;
        }
        drawCircle(canvas,height * mBigCirRatio / 2,mStartAngle,mSweepAngle);

        if (circleAniValue >= 1f) {
            //话对勾
            successPath.moveTo(width * (260f / 700f), width * (344f / 700f));
            successPath.lineTo(width * (350f / 700f), width * (435f / 700f));
            successPath.lineTo(width * (480f / 700f), width * (304f / 700f));
            pathMeasure.setPath(successPath, false);
            //起始位置的小圆点
            canvas.drawCircle(width * (260f / 700f), width * (344f / 700f), mStrokeWidth / 2, mCirclePaint);
            //对勾
            pathMeasure.getSegment(0, successAniValue * pathMeasure.getLength(), mPath, true);
            //跟随画笔的小圆点
            float x = 0, y = 0, tx = 0, ty = 0;
            float[] pos = {x, y};
            float[] tan = {tx, ty};
            pathMeasure.getPosTan(successAniValue * pathMeasure.getLength(), pos, tan);
            canvas.drawCircle(pos[0], pos[1], mStrokeWidth / 2, mCirclePaint);
            canvas.drawPath(mPath, paint);
        }
    }

    public void start(float angle) {
        this.mStartAngle = angle;
        reset();
        mIsWorking = true;
        animatorSet.start();
    }

    public void reset() {
        mIsWorking = false;
//        initPara();
        mPath.reset();
        mPath.moveTo(0, 0);
        postInvalidate();
    }

    public interface OnAnimEndListener {
        void onAnimEnd();
    }

    public void setOnAnimEndListener(OnAnimEndListener listener) {
        this.animEndListener = listener;
    }


    private void drawCircle(Canvas canvas, float radius, float startAngle,float sweepAngle) {
        //中心
        int centerX = width / 2;
        int centerY = height / 2;
        //弧线宽度
        //内弧线直径
        float diameter = radius * 2;
        //内弧线半径
        float radiusInCircle = diameter / 2 - mStrokeWidth / 2;

        // 画弧形的矩阵区域。
        float l = centerX - radius + mStrokeWidth / 2;
        float t = centerY - radius + mStrokeWidth / 2;
        float r = centerX + radius - mStrokeWidth / 2;
        float b = centerY + radius - mStrokeWidth / 2;
        RectF rectBg = new RectF(l, t, r, b);

        // 弧形ProgressBar。
        canvas.drawArc(rectBg, startAngle,sweepAngle, false, paint);

        //画起始的小圆
        // 计算圆心和半径。
        float cx_start = (float) (centerX + (radiusInCircle) * Math.cos(startAngle * Math.PI / 180)); //圆心的x坐标
        float cy_start = (float) (centerY + radiusInCircle * Math.sin(startAngle * Math.PI / 180)); //圆心y坐标  一样

        canvas.drawCircle(cx_start, cy_start, mStrokeWidth / 2, mCirclePaint);

        //画尾巴
        float x = (float) (centerX + radiusInCircle * Math.cos((sweepAngle + startAngle) * Math.PI / 180));
        float y = (float) (centerY + radiusInCircle * Math.sin((sweepAngle + startAngle) * Math.PI / 180));
        canvas.drawCircle(x, y, mStrokeWidth / 2, mCirclePaint);// 小圆

    }

}
