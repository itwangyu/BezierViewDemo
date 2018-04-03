package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * 弧形进度条
 *
 * @author Wangyu
 */
public class CircleProgressbar extends View {
    private static final String TAG = "wangyu";
    private Context context;
    private float barStrokeWidth = 10;
    private int barColor = Color.WHITE;
    private Paint mPaintCircle = null;
    private RectF rectBg = null;
    private int width;
    private int height;
    private float mBigRatio = 0.8f;
    private float mSmallRatio = 0.5f;
    private TypedArray mTypedArray;

    private Control bigControl =null;
    private Paint mPaintBar;
    private RoundCompleteListener mRoundCompleteListener;
    private boolean mIsWorking=false;


    public CircleProgressbar(Context context) {
        super(context);
        this.context = context;
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressbar, -1, 0);
        init();
    }

    private void init() {
        if (mTypedArray != null) {
            barColor = mTypedArray.getColor(R.styleable.CircleProgressbar_barColor, Color.WHITE);
            barStrokeWidth = mTypedArray.getDimension(R.styleable.CircleProgressbar_barStrokeWidth, barStrokeWidth);
            mSmallRatio = mTypedArray.getFloat(R.styleable.CircleProgressbar_barSmallRatio, mSmallRatio);
            mBigRatio = mTypedArray.getFloat(R.styleable.CircleProgressbar_barBigRatio, mBigRatio);
        }
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
        if (bigControl!=null) {
            drawBigCircle(canvas, width * mBigRatio/2 , bigControl);
            bigControl.updateUiByStatus();
        }
        if (mIsWorking) {
            postInvalidate();
        }
    }

    public void start() {
        bigControl= new Control(-90);
        mIsWorking=true;
        postInvalidate();
    }

    public void stop() {
        mIsWorking=false;
    }


    public interface RoundCompleteListener {
        void onRoundComplete();
    }

    public void setOnRoundCompleteListener(RoundCompleteListener listener) {
        this.mRoundCompleteListener=listener;
    }

    /**
     * 控制
     */
    class Control {
        private final float VALUE_DEFAULT_STEP=2;
        //起点弧度
        private double startAngle = 0;
        //默认的弧长
        private int sweepAngleOrignal = 360;
        private double sweepAngle = sweepAngleOrignal;


        //第一阶段参数
        private int fastStep = 4;
        private int fastRunAngle = 360;

        //第二阶段参数
        private float growStep = VALUE_DEFAULT_STEP;
        private double growLength = 360;

        //缩小参数
        private float smallStep = VALUE_DEFAULT_STEP;
        private double smallLength = sweepAngleOrignal;

        private float slowStep=0.1f;
        private int totalAngle = 0;
        private final int VALUE_STATE_SMALL_FAST=0,VALUE_STATE_GROW_UP=1, VALUE_STATE_LONG_FAST =2,VALUE_STATE_REDUCE_DOWN=3;
        private int mStatus = VALUE_STATE_REDUCE_DOWN;

        /**
         * 获取开始弧度位置
         *
         * @return
         */
        public double getStartAngle() {
            return startAngle;
        }

        private void changeState(int status) {
            mStatus=status;
        }

        /**
         * 获取弧度长度
         *
         * @return
         */
        public double getSweepAngle() {
            return sweepAngle;
        }

        public Control(double startAngle) {
            this.startAngle = startAngle;
        }

        /**
         * mStatus == 0 进入
         * 短快速转动一圈
         */
        private void firstStage() {
            if (startAngle > 360) {
                startAngle -= 360;
            }
            if (totalAngle < fastRunAngle) {
                startAngle += fastStep;
                totalAngle += fastStep;
            } else {
                totalAngle = 0;
                changeState(VALUE_STATE_GROW_UP);
            }
        }

        /**
         * mStatus == 1 进入
         */
        private void toGrowUp() {
//            if (startAngle > 360) {
//                startAngle -= 360;
//            }
            if (-sweepAngle < growLength) {
                startAngle += growStep;
                sweepAngle -= growStep-slowStep;
            } else {
                totalAngle = 0;
                changeState(VALUE_STATE_REDUCE_DOWN);
            }

        }

        /**
         * mStatus == 2 进入
         * 长的 很快转动 转2圈
         */
        private void keepLongFast() {
            if (startAngle > 360) {
                startAngle -= 360;
            }
            if (totalAngle < 24) {
                startAngle += fastStep;
                totalAngle += fastStep;
            } else {
                totalAngle = 0;
                changeState(VALUE_STATE_REDUCE_DOWN);
            }
        }

        /**
         * mStatus == 3 进入
         * 缩小
         */
        private void toSmall() {
//            if (startAngle > 360) {
//                startAngle -= 360;
//            }
            if (-sweepAngle > -smallLength) {
                startAngle += slowStep;
                sweepAngle += smallStep;
            } else {
                totalAngle = 0;
                if (mRoundCompleteListener != null) {
                    mRoundCompleteListener.onRoundComplete();
                }
                changeState(VALUE_STATE_GROW_UP);
            }
        }

        /**
         * 更新参数
         *
         * @return
         */
        private double updateUiByStatus() {
            if (mStatus == VALUE_STATE_SMALL_FAST) {
                //短小 快速转动
                firstStage();
            } else if (mStatus == VALUE_STATE_GROW_UP) {
                //基本保持尾部不动增长
                toGrowUp();
            } else if (mStatus == VALUE_STATE_LONG_FAST) {
                keepLongFast();
            } else if (mStatus == VALUE_STATE_REDUCE_DOWN) {
                toSmall();
            }
            return startAngle;
        }

    }


    /**
     * 画圆
     *
     * @param canvas
     * @param radius
     * @param control
     */
    private void drawBigCircle(Canvas canvas, float radius, Control control) {
        //中心
        int centerX = width / 2;
        int centerY = height / 2;
        //弧线宽度
        //内弧线直径
        float diameter = radius * 2 ;
        //内弧线半径
        float radiusInCircle = diameter / 2-barStrokeWidth/2;

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

