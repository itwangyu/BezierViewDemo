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

    private Control bigControl = new Control(0);
    private Control smallControl = new Control(100);
    private Paint mPaintBar;


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
        smallControl.setFastShortRunCircle(540);
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
        _draw(canvas);
        postInvalidate();
    }

    private void _draw(Canvas canvas) {
        drawBigCircle(canvas, width * mBigRatio/2 , bigControl);
//        drawBigCircle(canvas, width * mBigRatio *mSmallRatio/2, smallControl);

        getCurrentStartAngle();
    }

    private void getCurrentStartAngle() {
        bigControl.updateStatus();
        smallControl.updateStatus();
    }

    /**
     * 控制
     */
    class Control {
        //起点弧度
        private double startAngle = 0;
        //默认的弧长
        private int sweepAngleOrignal = -10;
        private int sweepAngle = sweepAngleOrignal;


        //第一阶段参数
        private int fastStep = 4;
        private int fastRunAngle = 360;

        //第二阶段参数
        private int growStep = 2;
        private double growLength = 270;

        //缩小参数
        private int smallStep = 2;
        private double smallLength = sweepAngleOrignal;

        private int status = 1;
        private int totalAngle = 0;

        public void setFastShortRunCircle(int circleAngle) {
            fastRunAngle = circleAngle;
        }

        /**
         * 获取开始弧度位置
         *
         * @return
         */
        public double getStartAngle() {
            return startAngle;
        }

        /**
         * 获取弧度长度
         *
         * @return
         */
        public int getSweepAngle() {
            return sweepAngle;
        }

        public Control(double startAngle) {
            this.startAngle = startAngle;
        }

        /**
         * status == 0 进入
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
                status = 1;
            }
        }

        /**
         * status == 1 进入
         */
        private void toGrowUp() {
//            Log.i("wangyu", "toGrowUp: "+startAngle);
            if (startAngle > 360) {
                startAngle -= 360;
            }
            if (-sweepAngle < growLength) {
                startAngle += growStep;
                sweepAngle -= growStep * 0.67;
            } else {
                totalAngle = 0;
                status = 2;
            }

        }

        /**
         * status == 2 进入
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
                status = 3;
            }
        }

        /**
         * status == 3 进入
         * 缩小
         */
        private void toSmall() {
            if (startAngle > 360) {
                startAngle -= 360;
            }
            if (-sweepAngle > smallLength) {
                startAngle += smallStep;
                sweepAngle += smallStep;
            } else {
                totalAngle = 0;
                status = 0;
            }
        }

        /**
         * 更新参数
         *
         * @return
         */
        private double updateStatus() {
            if (status == 0) {
                //短小 快速转动
                firstStage();
            } else if (status == 1) {
                //基本保持尾部不动增长
                toGrowUp();
            } else if (status == 2) {
                keepLongFast();
            } else if (status == 3) {
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
        canvas.drawArc(rectBg, (float) control.getStartAngle(), control.getSweepAngle(), false, mPaintBar);


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

