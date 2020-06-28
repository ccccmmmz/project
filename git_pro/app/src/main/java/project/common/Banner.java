package project.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Banner extends FrameLayout {
    public Banner(Context context) {
        super(context);
    }

    public Banner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Banner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
        System.out.print(String.format(Locale.getDefault(), "ligen --- init enter"));
    }

    private void init() {
        final ViewPager viewPager = new ViewPager(getContext());
        addView(viewPager);
        viewPager.setOffscreenPageLimit(1);
        final List<View> views = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(getContext());
            textView.setText("第几个" + i);
            views.add(textView);
        }
        TextView textView = new TextView(getContext());
        textView.setText("第几个" + 4);
        views.add(0, textView);
        TextView textView1 = new TextView(getContext());
        textView1.setText("第几个" + 0);
        views.add(textView1);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size() - 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                return views.get(position);
            }
        });
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                System.out.print(String.format(Locale.getDefault(), "ligen --- position = %d", position));
                if (position == 0) {
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                } else if (position == (viewPager.getAdapter().getCount() - 1)) {
                    viewPager.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }
}
