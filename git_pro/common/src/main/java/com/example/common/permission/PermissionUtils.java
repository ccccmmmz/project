package com.example.common.permission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 6.0 运行时权限处理工具类。
 */
public class PermissionUtils {
    /**
     * 请求权限对应的请求码
     */
    private static final int CALL_PHONE = 1;//拨打电话
    private static final int SM = 2;//短信
    private static final int CONTACTS = 3;//联系人

    private static int mRequestCode = -1;

    public static void requestPermissionsResult(Activity activity, int requestCode
            , String[] permission, OnPermissionListener callback) {
        requestPermissions(activity, requestCode, permission, callback);
    }

    public static void requestPermissionsResult(android.app.Fragment fragment, int requestCode
            , String[] permission, OnPermissionListener callback) {
        requestPermissions(fragment, requestCode, permission, callback);
    }

    public static void requestPermissionsResult(Fragment fragment, int requestCode
            , String[] permission, OnPermissionListener callback) {
        requestPermissions(fragment, requestCode, permission, callback);
    }

    /**
     * 请求权限处理
     *
     * @param object      activity or fragment
     * @param requestCode 请求码
     * @param permissions 需要请求的权限
     * @param callback    结果回调
     */

    @SuppressLint("NewApi")
    private static void requestPermissions(Object object, int requestCode
            , String[] permissions, OnPermissionListener callback) {

        checkCallingObjectSuitability(object);
        mOnPermissionListener = callback;

        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            if (mOnPermissionListener != null)
                mOnPermissionListener.onPermissionGranted();
        } else {

            if (checkPermissions(getContext(object), permissions)) {
                //权限全部允许
                if (mOnPermissionListener != null)
                    mOnPermissionListener.onPermissionGranted();
            } else {
                List<String> deniedPermissions = getDeniedPermissions(getContext(object), permissions);
                if (deniedPermissions.size() > 0) {
                    mRequestCode = requestCode;
                    if (object instanceof Activity) {
                        ((Activity) object).requestPermissions(deniedPermissions
                                .toArray(new String[0]), requestCode);
                    } else if (object instanceof android.app.Fragment) {
                        ((android.app.Fragment) object).requestPermissions(deniedPermissions
                                .toArray(new String[0]), requestCode);
                    } else if (object instanceof Fragment) {
                        ((Fragment) object).requestPermissions(deniedPermissions
                                .toArray(new String[0]), requestCode);
                    } else {
                        mRequestCode = -1;
                    }
                }
            }
        }

    }

    /**
     * 获取上下文
     */
    private static Context getContext(Object object) {
        Context context;
        if (object instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else {
            context = (Activity) object;
        }
        return context;
    }

    /**
     * 请求权限结果，对应onRequestPermissionsResult()方法。
     *
     * @param permissions 目前只有同一个权限组的处理 多个不想关的权限一起请求的话
     */
    public static void onRequestPermissionsResult(Activity context, int requestCode, String[] permissions, int[] grantResults) {
        if (mRequestCode != -1 && requestCode == mRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (mOnPermissionListener != null)
                    mOnPermissionListener.onPermissionGranted();
            } else {
                if (mOnPermissionListener != null) {
                    //有没有同意的 开发者只需要关注onPermissionDenied(true)的回调
                    //调用同一个权限组的所有权限的时候拒绝不再询问会反复调用callback.onPermissionDenied(true)
                    for (String permission : permissions) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                            mOnPermissionListener.onPermissionDenied(false);
                        } else {
                            //有一个永久拒绝了
                            mOnPermissionListener.onPermissionDenied(true);
                            return;
                        }
                    }
                }

            }
        }
    }

    /**
     * 显示提示对话框
     */
    public static void showTipsDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 验证权限是否都已经授权
     */
    private static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限列表中所有需要授权的权限
     *
     * @param context     上下文
     * @param permissions 权限列表
     */
    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 检查所传递对象的正确性
     *
     * @param object 必须为 activity or fragment
     */
    private static void checkCallingObjectSuitability(Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;

        if (!(isActivity || isSupportFragment || isAppFragment)) {
            throw new IllegalArgumentException(
                    "Caller must be an Activity or a Fragment");
        }
    }

    /**
     * 检查所有的权限是否已经被授权
     *
     * @param permissions 权限列表
     * @return 权限是否全部允许
     */
    private static boolean checkPermissions(Context context, String... permissions) {
        if (isOverMarshmallow()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断当前手机API版本是否 >= 6.0
     */
    private static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 拨打电话
     */
    public static void askCallPhonePermission(Activity mActivity, OnPermissionListener mOnPermissionListener) {
        requestPermissions(mActivity, CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE}, mOnPermissionListener);
    }

    /**
     * 发送短信
     */
    public static void askSmPermission(Activity mActivity, OnPermissionListener mOnPermissionListener) {
        requestPermissions(mActivity, SM, new String[]{Manifest.permission.SEND_SMS}, mOnPermissionListener);
    }

    /**
     * 联系人权限
     */
    public static void askContacts(Activity mActivity, OnPermissionListener mOnPermissionListener) {
        requestPermissions(mActivity, CONTACTS, new String[]{Manifest.permission.WRITE_CONTACTS}, mOnPermissionListener);
    }

    /**
     * 定位权限
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void askLocation(Activity mActivity, OnPermissionListener mOnPermissionListener) {
        requestPermissions(mActivity, CONTACTS, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        }, mOnPermissionListener);
    }

    /**
     * 相机权限
     */
    public static void askCameraOption(Activity activity, OnPermissionListener mOnPermissionListener) {
        requestPermissions(activity, CONTACTS, new String[]{
                Manifest.permission.CAMERA}, mOnPermissionListener);
    }

    /**
     * 悬浮框权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void askOverFlowWindow(Context context, OnPermissionListener mOnPermissionListene) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mOnPermissionListene.onPermissionGranted();
            return;
        }
        //有权限
        if (Settings.canDrawOverlays(context)) {
            mOnPermissionListene.onPermissionGranted();
            return;
        }
        Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName()));
        ((Activity) context).startActivityForResult(intent, 100);

    }


    /**
     * 判断 悬浮窗口权限是否打开
     * 19之后开放 19之前默认返回true
     *
     * @return true 允许 false禁止
     */
    @SuppressWarnings("unchecked")
    public static boolean getAppOps(Context context) {
        try {
            Object object;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                object = context.getSystemService(Context.APP_OPS_SERVICE);
            } else {
                Log.e("ligen", "getAppOps: below 19");
                return true;
            }
            if (object == null) {
                Log.e("ligen", "getAppOps: object is null");
                return true;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                Log.e("ligen", "getAppOps: getMethod is null");
                return true;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = (Integer) method.invoke(object, arrayOfObject1);
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("ligen", "getAppOps: exception" + ex.getMessage());
        }
        return false;
    }

    //回调
    public interface OnPermissionListener {
        void onPermissionGranted();

        //neverRequest 拒绝之后再也不会弹出
        void onPermissionDenied(boolean neverRequest);
    }

    private static OnPermissionListener mOnPermissionListener;

}
