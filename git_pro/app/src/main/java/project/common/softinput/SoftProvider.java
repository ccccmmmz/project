package project.common.softinput;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


public class SoftProvider implements LifecycleEventObserver {

    private static final String TAG = "SoftProvider";

    private Activity mActivity;

    private final View mRootView;

    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;

    private int mSoftHeight = 0;

    private final SoftProviderListener mSoftProviderListener;

    private int mVisibleHeight = 0;

    public SoftProvider(Activity activity, View rootView, SoftProviderListener mSoftProviderListener) {
        if (activity instanceof LifecycleOwner) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(this);
        }
        mActivity = activity;
        mRootView = rootView;
        this.mSoftProviderListener = mSoftProviderListener;
        mOnGlobalLayoutListener = () -> {

            Rect root = new Rect();
            mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(root);

            mVisibleHeight = mRootView.getBottom();

            Rect rect = new Rect();
            mRootView.getWindowVisibleDisplayFrame(rect);
            // REMIND, you may like to change this using the fullscreen size of the phone
            // and also using the status bar and navigation bar heights of the phone to calculate
            // the keyboard height. But this worked fine on a Nexus.
            mSoftHeight = mVisibleHeight - rect.bottom;
            notifySoftState(mSoftHeight);
        };
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }


    private void notifySoftState(int softHeight) {
        if (mSoftProviderListener != null) {
            mSoftProviderListener.SoftStateChange(softHeight);
        }
    }


    private void onDestroy() {
        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.getLifecycle().removeObserver(this);
            onDestroy();
        }
    }

    public interface SoftProviderListener {
        void SoftStateChange(int height);
    }
}
