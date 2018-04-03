package com.example.huangxiujun.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static android.animation.ValueAnimator.INFINITE;

/**
 * Created by WangYu on 2018/3/16.
 */

public class ConnectView extends RelativeLayout {
    private final Context mContext;

    private Animation mAnimationUp;
    private Animation mAnimationDown;
    private float degree = 0f;
    private ImageView mIVEarth;
    private ImageView mIVConnect;
    private ObjectAnimator rotation;
    private Animation mPeopleAnimDown;
    private Animation mPeopleAnimUp;
    private ImageView mIVPeople;
    private AnimationDrawable animationDrawable;

    public ConnectView(Context context) {
        this(context, null);
    }

    public ConnectView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ConnectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        View.inflate(mContext, R.layout.activity_mian_connect_view, this);
        mAnimationDown = AnimationUtils.loadAnimation(mContext, R.anim.anim_earth_down_big);
        mAnimationUp = AnimationUtils.loadAnimation(mContext, R.anim.anim_earth_up_small);
        mPeopleAnimDown = AnimationUtils.loadAnimation(mContext, R.anim.anim_people_down_big);
        mPeopleAnimUp = AnimationUtils.loadAnimation(mContext, R.anim.anim_people_up_small);
        mIVEarth = findViewById(R.id.iv_earth);
        mIVPeople = findViewById(R.id.iv_people);
        mIVConnect = findViewById(R.id.iv_connect);
        animationDrawable = (AnimationDrawable) mIVPeople.getBackground();
        mIVConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                startRotateAnim(5000);
            }
        });
    }

    public void startRotateAnim(int duration) {
        if (rotation != null) {
            rotation.cancel();
        }
        rotation = ObjectAnimator.ofFloat(mIVEarth, "rotation", degree, degree - 360f);
        rotation.setDuration(duration);
        rotation.setRepeatCount(INFINITE);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degree = (Float) animation.getAnimatedValue();
            }
        });
        rotation.start();
        animationDrawable.start();
    }

    public void startDownAnim(int duration) {
        mIVEarth.clearAnimation();
        mAnimationDown.setDuration(duration);
        mIVEarth.startAnimation(mAnimationDown);
        mPeopleAnimDown.setDuration(duration);
        mIVPeople.startAnimation(mPeopleAnimDown);
    }

    public void startUpAnim(int duration) {
        mIVEarth.clearAnimation();
        mAnimationUp.setDuration(duration);
        mIVEarth.startAnimation(mAnimationUp);
        mPeopleAnimUp.setDuration(duration);
        mIVPeople.startAnimation(mPeopleAnimUp);
    }

    public void stopRotateAnim() {
        if (rotation != null)
            rotation.cancel();
        if (animationDrawable != null)
            animationDrawable.stop();
    }

}
