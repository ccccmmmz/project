package project.common.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class BaseVoiceBehavior<T extends View> extends CoordinatorLayout.Behavior<T> {

    protected Context mContext;

    public BaseVoiceBehavior() {

    }

    public BaseVoiceBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull T child, @NonNull View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }


}
