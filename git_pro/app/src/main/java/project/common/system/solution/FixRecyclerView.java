package project.common.system.solution;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FixRecyclerView extends RecyclerView {

    public FixRecyclerView(@NonNull Context context) {
        super(context);
    }

    public FixRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        if (getOverScrollMode() != RecyclerView.OVER_SCROLL_NEVER) {
            setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        }
        if (layout instanceof GridLayoutManager) {
            LinearLayoutManager layout1 = (LinearLayoutManager) layout;
            super.setLayoutManager(new FixGridLayoutManager(getContext(), ((GridLayoutManager) layout1).getSpanCount(), layout1.getOrientation(), layout1.getReverseLayout()));
            return;
        } else if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(new FixLinearManager(getContext(), ((LinearLayoutManager) layout).getOrientation(), ((LinearLayoutManager) layout).getReverseLayout()));
            return;
        }
        super.setLayoutManager(layout);
    }
}
