package com.test.commonporject.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TestCanvas extends View {
    Paint mPaint;

    public TestCanvas(Context context) {
        super(context);
    }

    public TestCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, 400, 400, mPaint);
        canvas.restore();

        canvas.save();
        canvas.scale(0.5f, 0.5f);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, 400, 400, mPaint);
        canvas.restore();


        canvas.save();
        canvas.scale(0.5f, 0.5f, 200, 200);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, 400, 400, mPaint);
        canvas.restore();

        mPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, 300, 300, mPaint);

    }
}
