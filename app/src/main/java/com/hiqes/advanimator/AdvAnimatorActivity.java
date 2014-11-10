package com.hiqes.advanimator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Property;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;


public class AdvAnimatorActivity extends Activity {
    private static final int    BG_COLOR_START = (int)0xFF1D92DB;
    private static final int    BG_COLOR_REVERSE_POINT = (int)0xFF6DA7BD;
    private static final int    BG_COLOR_RESUME_POINT = (int)0xFF409BBD;
    private static final int    BG_COLOR_END = (int)0xFFA5B7BD;
    private static final int    ANIM_DURATION = 1500;
    private ObjectAnimator      mColorAnim1;
    private Button              mAnimButton1;
    private View                mColorView;
    private ColorDrawable       mBg;

    private ObjectAnimator      mColorAnim2;
    private Button              mAnimButton2;
    private View                mWashView;
    private ColorDrawable       mWashBg;

    private ObjectAnimator      mColorAnim3;
    private Button              mAnimButton3;
    private View                mMultiWashView;
    private ColorDrawable       mMultiWashBg;

    private LinearLayout        mLayout;
    private LayoutTransition    mLT;
    private Animator            mAppearAnim;
    private Button              mAppearBtn;
    private View                mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_animator);

        //  Simple animation of the background color as a plain
        //  integer - NOT what we want!
        mColorView = findViewById(R.id.view_color_cycle);
        mBg = new ColorDrawable(BG_COLOR_START);
        mColorView.setBackground(mBg);
        mColorAnim1 =
            ObjectAnimator.ofInt(mBg,
                                 "color",
                                 BG_COLOR_START,
                                 BG_COLOR_END);
        mColorAnim1.setDuration(ANIM_DURATION);

        mAnimButton1 = (Button)findViewById(R.id.btn_color_cycle);
        mAnimButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorAnim1.start();
            }
        });

        //  Simple animation of the background color to make it "wash out"
        //  by using a custom TypeEvaluator
        mWashView = findViewById(R.id.view_wash);
        mWashBg = new ColorDrawable(BG_COLOR_START);
        mWashView.setBackground(mWashBg);
        PropertyValuesHolder colorHolder =
            PropertyValuesHolder.ofObject("color",
                                          new ArgbEvaluator(),
                                          Integer.valueOf(BG_COLOR_START),
                                          Integer.valueOf(BG_COLOR_END));
        mColorAnim2 =
            ObjectAnimator.ofPropertyValuesHolder(mWashBg, colorHolder);
        mColorAnim2.setDuration(ANIM_DURATION);

        mAnimButton2 = (Button)findViewById(R.id.btn_wash);
        mAnimButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorAnim2.start();
            }
        });

        //  More advanced animation of the background color wash out
        //  using Keyframes and different TimeInterpolators
        mMultiWashView = findViewById(R.id.view_multi_wash);
        mMultiWashBg = new ColorDrawable(BG_COLOR_START);
        mMultiWashView.setBackground(mMultiWashBg);
        Keyframe startFrame = Keyframe.ofInt(0.0f, BG_COLOR_START);
        startFrame.setInterpolator(new AccelerateInterpolator());
        Keyframe reverseFrame = Keyframe.ofInt(0.3f, BG_COLOR_REVERSE_POINT);
        reverseFrame.setInterpolator(new DecelerateInterpolator());
        Keyframe resumeFrame = Keyframe.ofInt(0.6f, BG_COLOR_RESUME_POINT);
        resumeFrame.setInterpolator(new DecelerateInterpolator());
        Keyframe endFrame = Keyframe.ofInt(1.0f, BG_COLOR_END);
        endFrame.setInterpolator(new OvershootInterpolator());
        PropertyValuesHolder multiHolder =
            PropertyValuesHolder.ofKeyframe("color", startFrame, reverseFrame, endFrame);
        multiHolder.setEvaluator(new ArgbEvaluator());
        mColorAnim3 =
            ObjectAnimator.ofPropertyValuesHolder(mMultiWashBg, multiHolder);
        mColorAnim3.setDuration(ANIM_DURATION);

        mAnimButton3 = (Button)findViewById(R.id.btn_multi_keys);
        mAnimButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorAnim3.start();
            }
        });

        //  Load the image we are going to make "appear" or "disappear"
        mImg = findViewById(R.id.lt_img);

        //  Load the custom "appear" animation and create a LayoutTransition
        mAppearAnim = AnimatorInflater.loadAnimator(this, R.animator.custom_appear);
        mLayout = (LinearLayout)findViewById(R.id.container);
        mLT = new LayoutTransition();
        mLT.setAnimator(LayoutTransition.APPEARING, mAppearAnim);

        //  Set the start delay of our custom animation to be the
        //  duration of the "change appearing" animation so it does
        //  not begin before the other views have "made room".
        Animator ca = mLT.getAnimator(LayoutTransition.CHANGE_APPEARING);
        mAppearAnim.setStartDelay(ca.getDuration());
        mLayout.setLayoutTransition(mLT);

        //  Use a button to trigger the image to come or go
        mAppearBtn = (Button)findViewById(R.id.btn_lt);
        mAppearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImg.getVisibility() == View.GONE) {
                    mImg.setAlpha(0f);
                    mImg.setVisibility(View.VISIBLE);
                } else {
                    mImg.setVisibility(View.GONE);
                }
            }
        });

    }
}
