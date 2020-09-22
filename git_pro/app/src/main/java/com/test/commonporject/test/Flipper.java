package com.test.commonporject.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.test.commonporject.R;

public class Flipper extends LinearLayout {

    private int mShowCount = 2;


    private boolean mAutoStart = true;

    private int mInterval = 2500;

    private int mWhichChild = 1;

    private Context mContext;

    private View mRemoveView;
    Animation mInAnimation;
    Animation mOutAnimation;

    private boolean mInitState = true;

    public Flipper(Context context) {
        this(context, null);
    }

    public Flipper(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Flipper(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_in_vertical);
        mInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //进入完毕 添加view
                View childAt = getChildAt(2);
                System.out.println("可见" + childAt.getVisibility());

                //System.out.println("移除view");
                //removeView(mRemoveView);
                if (!mInitState) {
                    //addView(mRemoveView);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mInAnimation.setFillAfter(true);
        mOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_out_vertical);
        mOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                View childAt = getChildAt(0);
                LinearLayout.LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();

                float translationY = childAt.getTranslationY();
                System.out.println("ligen,y"+ layoutParams.toString());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mOutAnimation.setFillAfter(true);
        if (mAutoStart) {
            postDelayed(mNextRunnable, mInterval);
        }
    }

    private Runnable mNextRunnable = new Runnable() {
        @Override
        public void run() {
            showNext();
//            postDelayed(mNextRunnable, mInterval);
        }
    };

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (mRemoveView == null && getChildCount() == 2) {
            mRemoveView = child;
            return;
        }
        super.addView(child, params);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        if (mWhichChild != 0) {
            mWhichChild--;
        }

    }

    private void showNext() {
        setDisplayedChild(mWhichChild + 1);
    }

    public void setDisplayedChild(int whichChild) {
        mWhichChild = whichChild;
        if (whichChild >= getChildCount()) {
            mWhichChild = 0;
        } else if (whichChild < 0) {
            mWhichChild = getChildCount() - 1;
        }
        showOnly(mWhichChild, true);
    }

    void showOnly(int childIndex, boolean animate) {
        final int count = getChildCount();
        System.out.println("ligen,view count = " + count);

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (i == childIndex) {
                child.setVisibility(View.VISIBLE);
                if (animate && mInAnimation != null) {
                    child.startAnimation(mInAnimation);
                    System.out.println("show" + i);
                }

                //mFirstTime = false;
            } else {
                if (child == null) {
                    return;
                }
                if (animate && mOutAnimation != null && child.getVisibility() == View.VISIBLE) {
                    child.startAnimation(mOutAnimation);
                } else if (child.getAnimation() == mInAnimation)
                    child.clearAnimation();
                if (getUnShowIndex() == i) {
                    //child.setVisibility(View.GONE);
                    mRemoveView = child;
                    if (mInitState) {
                        mInitState = false;
                        System.out.println("ligen,出去动画" + i);
                        if (child.getAnimation() != mOutAnimation) {
                            child.startAnimation(mOutAnimation);
                            System.out.println("ligen,出去动画" + i);
                        }
                    } else {
                        //removeView(child);
                        System.out.println("ligen,移除 = " + i);
                    }

                } else {
                    System.out.println("不show" + i);
                    child.setVisibility(View.VISIBLE);


                }

            }
        }
        System.out.println("-----------------");
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int drawingPosition) {
        return super.getChildDrawingOrder(childCount, drawingPosition);
    }

    private int getUnShowIndex() {
        //显示的view
        return (mWhichChild + mShowCount) % getChildCount();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("draw enter---");
    }
}
