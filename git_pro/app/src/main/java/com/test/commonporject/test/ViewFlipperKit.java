package com.test.commonporject.test;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * 纵向
 * viewFlipperKit.setInAnimation(this, R.anim.anim_in_vertical);
 * viewFlipperKit.setOutAnimation(this, R.anim.anim_out_vertical);
 * 横向
 * viewFlipperKit.setInAnimation(this,R.anim.anim_enter_in);
 * viewFlipperKit.setOutAnimation(this,R.anim.anim_out_out);
 */

public class ViewFlipperKit extends ViewFlipper {

    public ViewFlipperKit(Context context) {
        super(context, null);
    }

    public ViewFlipperKit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setScrollIndicators(SCROLL_INDICATOR_BOTTOM);
        }
        //setInAnimation(getContext(), R.anim.anim_enter_in);
    }

    public void initWithViews(List<View> views) {
        if (views == null || views.size() == 0) {
            throw new IllegalArgumentException("initWithViews with empty views");
        }
        for (View view : views) {
            //addView();
            addViewOption(view);
        }

        startFlipping();
    }

    public void addViewOption(View view) {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        handleTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void handleTouchEvent(MotionEvent ev) {
        float rawX, rawY;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rawX = ev.getX();
                rawY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                stopFlipping();


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                int pointerCount = ev.getPointerCount();
                if (pointerCount == 1) {
                    startFlipping();
                }
                break;
        }
    }
}
