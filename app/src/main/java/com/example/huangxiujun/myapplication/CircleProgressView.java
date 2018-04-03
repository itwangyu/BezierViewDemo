package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WangYu on 2018/3/2.
 */

public class CircleProgressView extends View {
    private static final String TAG = "wangyu";
    private Context context;
    private float barStrokeWidth = 10;
    private int barColor = Color.WHITE;
    private Paint mPaintCircle = null;
    private RectF rectBg = null;
    private int width;
    private int height;
    private TypedArray mTypedArray;
    private Paint mPaintBar;
    private boolean mIsWorking = false;
    private CircleControler circleControler = new CircleControler();
    private RoundCompleteListener mRoundCompleteListener;

    public CircleProgressView(Context context) {
        super(context);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(barColor);
        mPaintCircle.setStrokeWidth(barStrokeWidth);
        mPaintBar = new Paint();
        mPaintBar.setAntiAlias(true);
        mPaintBar.setStyle(Paint.Style.STROKE);
        mPaintBar.setStrokeWidth(barStrokeWidth);
        mPaintBar.setColor(barColor);
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
        drawCircle(canvas, width * 0.8f / 2, circleControler);
        circleControler.upDateUi();
        if (mIsWorking) {
            postInvalidate();
        }
    }

    public void start() {
        mIsWorking = true;
        postInvalidate();
    }

    public void stop() {
        mIsWorking = false;
    }

    public interface RoundCompleteListener {
        void onRoundComplete();
    }

    public void setOnRoundCompleteListener(RoundCompleteListener listener) {
        this.mRoundCompleteListener = listener;
    }

    public class CircleControler {
        private final float VALUE_FLOAT_SWEEP_ANGLE = 360;
        private float mPoint = -90;
        private float mStartAngle = -90;
        private float mEndAngle = mStartAngle + VALUE_FLOAT_SWEEP_ANGLE;
        private float mStep = 6;
        private float mPointStep = 0.2f;
        private int status = 0;

        public void upDateUi() {
            mPoint += mPointStep;
            if (status == 0) {
                reduce();
            } else if (status == 1) {
                grow();
            }
        }

        private void grow() {
            mStartAngle = mPoint;
            if (mEndAngle < mStartAngle + VALUE_FLOAT_SWEEP_ANGLE) {
                mEndAngle += mStep;
                if (mEndAngle > mStartAngle + VALUE_FLOAT_SWEEP_ANGLE) {
                    mEndAngle = mStartAngle + VALUE_FLOAT_SWEEP_ANGLE;
                    mStartAngle = mPoint;
                    status = 0;
                }
            }
        }

        private void reduce() {
            mEndAngle = mPoint + VALUE_FLOAT_SWEEP_ANGLE;
            if (mStartAngle < mEndAngle) {
                mStartAngle += mStep;
                if (mStartAngle > mEndAngle) {
                    mStartAngle = mPoint;
                    mEndAngle = mPoint;
                    status = 1;
                    if (mRoundCompleteListener != null) {
                        mRoundCompleteListener.onRoundComplete();
                    }
                }
            }
        }


        public float getStartAngle() {
            return mStartAngle;
        }

        public float getSweepAngle() {
            return mEndAngle - mStartAngle;
        }
    }

    /**
     * 画圆
     *
     * @param canvas
     * @param radius
     * @param control
     */
    private void drawCircle(Canvas canvas, float radius, CircleControler control) {
        //中心
        int centerX = width / 2;
        int centerY = height / 2;
        //弧线宽度
        //内弧线直径
        float diameter = radius * 2;
        //内弧线半径
        float radiusInCircle = diameter / 2 - barStrokeWidth / 2;

        // 画弧形的矩阵区域。
        float l = centerX - radius + barStrokeWidth / 2;
        float t = centerY - radius + barStrokeWidth / 2;
        float r = centerX + radius - barStrokeWidth / 2;
        float b = centerY + radius - barStrokeWidth / 2;
        rectBg = new RectF(l, t, r, b);

        // 弧形ProgressBar。
        canvas.drawArc(rectBg, (float) control.getStartAngle(), (float) control.getSweepAngle(), false, mPaintBar);

        //画起始的小圆
        // 计算圆心和半径。
        float cx_start = (float) (centerX + (radiusInCircle) * Math.cos(control.getStartAngle() * Math.PI / 180)); //圆心的x坐标
        float cy_start = (float) (centerY + radiusInCircle * Math.sin(control.getStartAngle() * Math.PI / 180)); //圆心y坐标  一样

        canvas.drawCircle(cx_start, cy_start, barStrokeWidth / 2, mPaintCircle);

        //画尾巴
        float x = (float) (centerX + radiusInCircle * Math.cos((control.getSweepAngle() + control.getStartAngle()) * Math.PI / 180));
        float y = (float) (centerY + radiusInCircle * Math.sin((control.getSweepAngle() + control.getStartAngle()) * Math.PI / 180));
        canvas.drawCircle(x, y, barStrokeWidth / 2, mPaintCircle);// 小圆

    }
}
