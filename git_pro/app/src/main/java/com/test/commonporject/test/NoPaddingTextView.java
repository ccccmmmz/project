package com.test.commonporject.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * https://juejin.im/post/5c875eb8e51d4511501e70e5
 * TextView适配
 */
public class NoPaddingTextView extends AppCompatTextView {

    //private boolean noPadding = true;
    //boolean ignore = false;
    //Paint.FontMetricsInt fontMetricsInt;
    private float scaleSize = 0;

    public NoPaddingTextView(Context context) {
        super(context);
        init();
    }

    public NoPaddingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoPaddingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        if (fontMetricsInt == null) {
//            fontMetricsInt = new Paint.FontMetricsInt();
//            getPaint().getFontMetricsInt(fontMetricsInt);
//            Log.i("ligen", "init: " + fontMetricsInt.toString());
//        }
        scaleSize = getIncludeFontPadding() ? 1.327f : 1.171f;
        float rawSize = getTextSize();
        float v = rawSize / scaleSize;
        Log.i("ligen", "init: xml = " + rawSize + "修改后 = " + v);
        setTextSize(COMPLEX_UNIT_PX, v);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        //ignore = mode == View.MeasureSpec.EXACTLY;
        //noPadding = mode != View.MeasureSpec.EXACTLY && getPaddingBottom() == 0 && getPaddingTop() == 0;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //float translationY = getTranslationY();
        //setTranslationY(fontMetricsInt.top - fontMetricsInt.ascent + translationY);
        super.onDraw(canvas);
    }
}
