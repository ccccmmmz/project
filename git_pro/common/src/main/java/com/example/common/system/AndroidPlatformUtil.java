package com.example.common.system;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

/**
 * Created by ligen
 * date:2018/11/21
 * 描述:android 各个版本兼容处理
 */
public class AndroidPlatformUtil {

    public static void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // android 7.0系统解决拍照的问题
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    //裁剪ViewGroup
    public static void roundViewGroup(View view, float radiusPx) {
        if (!(view instanceof ViewGroup)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setClipToOutline(true);
            view.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);//将view的区域保存在rect中
                    Rect selfRect = new Rect(0, 0, rect.right - rect.left, rect.bottom - rect.top);//绘制区域
                    outline.setRoundRect(selfRect, radiusPx);
                }
            });
        }

    }

}
