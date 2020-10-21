package project.common.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class BaseBehavior<T extends View> extends CoordinatorLayout.Behavior<T> {

    public BaseBehavior() {

    }

    public BaseBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull T child, @NonNull View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }
}
