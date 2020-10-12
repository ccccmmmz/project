package com.test.commonporject.test;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class FlipperCompat  extends RecyclerView {

    private Context mContext;
    private LinearLayoutManager mLinearLayoutManager;

    private int mWhichShow = 2;

    public FlipperCompat(@NonNull  Context context) {
        this(context,null);
    }

    public FlipperCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlipperCompat(@NonNull  Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mLinearLayoutManager = new LinearLayoutManager(mContext){
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
                LinearSmoothScroller linearSmoothScroller =
                        new LinearSmoothScroller(recyclerView.getContext()){
                            @Nullable
                            @Override
                            public PointF computeScrollVectorForPosition(int targetPosition) {
                                return super.computeScrollVectorForPosition(targetPosition);
                            }

                            @Override
                            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                return 3;
                            }
                        };
                linearSmoothScroller.setTargetPosition(position);
                startSmoothScroll(linearSmoothScroller);
            }
        };

        setLayoutManager(mLinearLayoutManager);
        setAdapter(new com.test.commonporject.test.Adapter());


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        showNext();
    }

    private void showNext(){
        postDelayed(() -> {
            if (mWhichShow == getAdapter().getItemCount()) {
                scrollToPosition(0);
                mWhichShow = 1;
            }
            smoothScrollToPosition(mWhichShow);
            System.out.println("show--"+mWhichShow);

            mWhichShow++;

            showNext();
        },5000);
    }
}
