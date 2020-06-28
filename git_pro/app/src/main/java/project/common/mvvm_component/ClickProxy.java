package project.common.mvvm_component;

import android.os.SystemClock;
import android.view.View;

public abstract class ClickProxy implements View.OnClickListener {
    private static final int DURATION = 500;
    private long mPreviousClick = 0;

    @Override
    public void onClick(View v) {
        long currentTime = SystemClock.uptimeMillis();
        if (currentTime - mPreviousClick >= DURATION) {
            mPreviousClick = currentTime;
            onFilterClick(v);
        } else {
            mPreviousClick = currentTime;
        }

    }

    public abstract void onFilterClick(View view);
}
