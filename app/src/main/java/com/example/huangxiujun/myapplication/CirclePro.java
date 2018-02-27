package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WangYu on 2018/1/30.
 */

public class CirclePro extends View {
    private int width;
    private int height;
    private Paint paint;
    private RectF rectF;

    private float startAngle=0,sweepAngle=0;
    private final String TAG="wangyu";
    public CirclePro(Context context) {
        super(context);
    }

    public CirclePro(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CirclePro(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF == null) {
            rectF = new RectF(5, 5, width - 5, height - 5);
        }
        canvas.drawArc(rectF,startAngle,sweepAngle,false,paint);
        grow();
    }

    private void grow() {
        switch (status) {
            case 0:
                grwoUp();
                break;
            case 1:
                growDown();
                break;
        }
        postInvalidate();
    }

    private void grwoUp() {
        if (Math.abs(sweepAngle) <= 300) {
            startAngle += 2;
            sweepAngle -= 1;
        } else {
            //长大了
            status=1;
        }
    }
    private int status=0;
    private void growDown() {
        if (Math.abs(sweepAngle) >= 5) {
            startAngle += 2;
            sweepAngle += 2;
        } else {
           status=0;
        }
    }
}
