package project.common.system;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;


import project.common.system.solution.FixRecyclerView;
import project.common.system.solution.FixViewPager;

public class IssueFix {

    public static void init(AppCompatActivity activity) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(activity), new LayoutInflater.Factory2() {
            @Nullable
            @Override
            public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                if (TextUtils.equals(name, "android.support.v7.widget.RecyclerView") || TextUtils.equals(name, "androidx.recyclerview.widget.RecyclerView")) {
                    return new FixRecyclerView(context, attrs);
                }
                if (TextUtils.equals(name, "androidx.viewpager.widget.ViewPager") || TextUtils.equals(name, "android.support.v7.widget.ViewPager")) {
                    return new FixViewPager(context, attrs);
                }
                return activity.getDelegate().createView(parent, name, context, attrs);
            }

            @Nullable
            @Override
            public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                return onCreateView(null, name, context, attrs);
            }
        });
    }
}
