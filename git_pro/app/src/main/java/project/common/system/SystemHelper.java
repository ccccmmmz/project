package project.common.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SystemHelper{

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    /**
     * @return 获取系统状态栏高度
     */
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context
                .getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context
                    .getResources()
                    .getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     *
     * @return  获取导航栏高度
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    /**
     *
     * @return 导航栏是否在显示
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasNavigation(WindowManager windowManager){
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }
    
     /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @param window  当前窗口
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean checkNavigationBarShow(@NonNull Context context, @NonNull Window window) {
        boolean show;
        Display display = window.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        View decorView = window.getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            show = (point.x != contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            show = (rect.bottom != point.y);
        }
        return show;
    }
 /**
     * 通过实例工厂去实例化相应类.
     *
     * @param o   带泛型的对象
     * @param i   需要实例化的对象在泛型中的位置
     * @param <T> 返回实例的泛型类型
     * @return
     */
    public static <T> T getInstance(Object o, int i) {
        try {
            Class<?> clazz = o.getClass();
            //ParameterizedType参数化类型，即泛型
            ParameterizedType p = getParamType(clazz);
            if(p == null) return null;
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            Type[] types = p.getActualTypeArguments();
            //获取需要的class
            Class<T> result = (Class<T>) types[i];
            return result.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ParameterizedType getParamType(Class<?> clazz) {
        if(clazz == null) return null;
        //getGenericSuperclass()获得带有泛型的父类
        //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
        if(clazz.getGenericSuperclass() instanceof ParameterizedType) {
            return (ParameterizedType) clazz.getGenericSuperclass();
        } else {
            return getParamType(clazz.getSuperclass());
        }
    }

    //service在桌面启动activity慢的方案
    static void startActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();

        }
    }

    public static void startActivity(Intent intent, Context context) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();

        }
    }

    //分享网络图片到系统app
    public static void shareNetImage(Activity context, String url) {
        //url->bitmap
        shareNetImage(context, (Bitmap) null);
    }


    public static void shareNetImage(Activity context, Bitmap bitmap) {
        //由文件得到uri
        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 关闭输入法
     *
     * @param view 对应的获取焦点的view
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //显示输入法
    public static void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 2);
    }

    //输入法是否显示 没有view的话可能一直是true
    public static boolean softIsShow(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm != null && imm.isActive() && imm.isActive(view);
    }

    /**
     * 获取当前进程名
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    //是否是主线程
    public static boolean isMainProcess(Context context) {
        return context != null && TextUtils.equals(context.getApplicationContext().getPackageName(), getCurrentProcessName(context));
    }

    //get current rotation
    public static int getRotationDegree(Activity context) {
        return 90 * (context.getWindowManager().getDefaultDisplay())
                .getRotation();
    }
}
