package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WangYu on 2018/1/18.
 */

public class ColorfulButton extends View {
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mTranslate = 0;
    private int mColor1=0xff8E92F7,mColor2=0xff4AE1E4;
    private RectF rectF;
    private int baseline;
    private String text;
    private Paint textPaint;
    private Rect bounds;

    public ColorfulButton(Context context) {
        super(context);
        initGradient();
    }

    public ColorfulButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGradient();
    }

    public ColorfulButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGradient();
    }

    private int width;
    private int height;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(rectF,height/2,height/2,mPaint);
        canvas.drawText(text,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, textPaint);
//        if (mGradientMatrix != null){
//            mTranslate += mViewWidth /20;
//            mGradientMatrix.setTranslate(mTranslate ,0);
//            mLinearGradient.setLocalMatrix(mGradientMatrix);
//            postInvalidateDelayed(50);
//        }
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if(mViewWidth == 0){
//            mViewWidth = getMeasuredWidth();
//            if (mViewWidth > 0 ){
//                initGradient();
//            }
//        }
//    }

    private void initGradient() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        String text = "SALFDKAFJD";
        textPaint.setTextSize(60);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.LEFT);
        bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        rectF = new RectF(0, 0, width, height);
        mLinearGradient = new LinearGradient(0,0,width,0,new int[]{mColor1,mColor2},null, Shader.TileMode.MIRROR);
        mGradientMatrix = new Matrix();
        mPaint.setShader(mLinearGradient);
    }

}
