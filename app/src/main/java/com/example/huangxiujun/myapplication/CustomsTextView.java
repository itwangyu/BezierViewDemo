package com.example.huangxiujun.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by HuangXiujun on 2018/1/15.
 */

/** * Created by keithXiaoY  */
// Java代码
public class CustomsTextView extends android.support.v7.widget.AppCompatTextView {
    private int mViewWidth = 0;
    private TextPaint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mTranslate = 0;
    private int mColor1=0xff8E92F7,mColor2=0xff4AE1E4;
    private boolean mWorking=false;

    public CustomsTextView(Context context) {
        this(context,null,0);
    }
    public CustomsTextView(Context context, AttributeSet attrs) {
        this(context, attrs ,0 );
    }
    public CustomsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatrix != null&&mWorking){
            mTranslate += mViewWidth /20;
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
        mPaint.setShader(mLinearGradient);
        postInvalidateDelayed(50);
    }
    public void stop() {
        mWorking=false;
        mPaint.setShader(null);
        mTranslate=0;
        postInvalidateDelayed(50);
    }
}
