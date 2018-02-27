package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by WangYu on 2018/1/16.
 */

public class ColorfulTextView extends android.support.v7.widget.AppCompatTextView {
    private int mViewWidth = 0;
    private TextPaint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mTranslate = 0;
    private int mColor1= Color.GREEN,mColor2=Color.RED;
    private boolean mWorking=false;

    public ColorfulTextView(Context context) {
        this(context,null,0);
    }
    public ColorfulTextView(Context context, AttributeSet attrs) {
        this(context, attrs ,0 );
    }
    public ColorfulTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatrix != null&&mWorking){
            mTranslate += mViewWidth /40;
            mGradientMatrix.setTranslate(mTranslate ,0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(50);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mViewWidth == 0){
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0 ){
                initGradient();
            }
        }
    }

    private void initGradient() {
        mPaint = getPaint();
        //Shader.TileMode.MIRROR   镜子，反射，反映
        //Shader.TileMode.REPEAT  重复
        mLinearGradient = new LinearGradient(0,0,mViewWidth,0,new int[]{mColor1,mColor2},null, Shader.TileMode.MIRROR);
        mGradientMatrix = new Matrix();
    }

    public void start() {
        mWorking=true;
        if (mPaint != null) {
            mPaint.setShader(mLinearGradient);
        }
        postInvalidateDelayed(50);
    }
    public void stop() {
        mWorking=false;
        if (mPaint != null) {
            mPaint.setShader(null);
        }
        mTranslate=0;
        postInvalidateDelayed(50);
    }
}
