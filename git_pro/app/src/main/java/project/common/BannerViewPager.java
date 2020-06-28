package project.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * BannerViewPager 实现轮播 就是添加了2个替代数据
 * ep 只有3个数据时 viewpager 内部真实数据时 2 0 1 2 0
 * 当滑动到最后一项的时候就静默切换前面,滑动到第一项的时候类似
 */
public class BannerViewPager extends ViewPager {
    private List<View> mViews;
    private boolean schedule;//是否调度
    private boolean enableSchedule = true;

    public BannerViewPager(@NonNull Context context) {
        super(context);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public void init(final List<View> views) {
        if (mViews == null) {
            mViews = new ArrayList<>();
            mViews.addAll(views);
        }
        if (views.size() == 1) {
            enableSchedule = false;
        }
        mViews.add(0, views.get(views.size() - 1));
        mViews.add(views.get(0));
        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public View instantiateItem(@NonNull ViewGroup container, int position) {
                View view = null;
                view = mViews.get(position);
                if (view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                View view = mViews.get(position);
                container.removeView(view);
            }
        });

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!enableSchedule) {
                    return;
                }
                if (schedule) {
                    schedule = false;
                    return;
                }
                if (position == views.size() - 1) {
                    schedule = true;
                    setCurrentItem(1, false);
                } else if (position == 0) {
                    schedule = true;
                    setCurrentItem(views.size() - 2, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
