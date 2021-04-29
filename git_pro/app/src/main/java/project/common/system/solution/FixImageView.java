package project.common.system.solution;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class FixImageView extends AppCompatImageView {
    public FixImageView(@NonNull Context context) {
        super(context);
    }

    public FixImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        try {
            super.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            super.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable dr) {
        try {
            super.invalidateDrawable(dr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
