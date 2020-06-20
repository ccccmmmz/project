package com.test.commonporject;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.common.DbUtils.BaseDaoImpl;
import com.example.common.permission.PermissionUtils;
import com.example.common.test.OrderBean;
import com.example.kotlinmodule.KTMainActivity;
import com.mmkv.MMKVGetter;
import com.test.commonporject.vmtest.ViewModelAct;

import java.util.List;

import util.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BaseDaoImpl<OrderBean> dao;
    ViewPager viewPager;
    boolean schedule = false;

    LinearLayout mLlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLlContent = findViewById(R.id.ll_content);
        Utils.init(this);
        test();
    }

    private void test() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                i -> {
                    int height = decorView.getHeight();
                    Log.i("ligen", "Current height: " + height);
                });
    }

    public void toggleHideyBar() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.i(TAG, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }

    public void testHttp() {
        //ApiKit.execute(ApiKit.getInstance().create(ApiService.class).test(), new ApiDisposableObserver<Object>() {
        //    @Override
        //    public void onResult(Object o) {
        //        System.out.println(o);
        //    }
        //
        //    @Override
        //    public void onError(ResponseThrowable responseThrowable) {
        //
        //    }
        //});
    }
    public void insert(View view) {
        for (int i = 0; i < 100; i++) {
            OrderBean userBean = new OrderBean(i, "地" + i);
            dao.insert(userBean);
        }

    }

    public void query(View view) {
        //UserBean update = new UserBean();
        //List<UserBean> query = dao.query();
        //Log.i("ligen", "查询到的size"+query.size());
//        UserBean userBean = query.get(10);
//        Log.i("ligen", "10条数据"+ userBean.toString());
        OrderBean bean1 = new OrderBean(10, "修改10111");
        List<OrderBean> query = dao.query();
        for (OrderBean bean : query) {
            Log.i("ligen", "query" + bean.toString());

        }
    }

    public void update(View view) {
//        UserBean update = new UserBean(3, "修改的");
//        dao.update(update);
        OrderBean bean = new OrderBean(10, "修改10111");
        dao.update(bean);
    }

    //app信息
    public void appInfo(View view) {
        PermissionUtils.startAppSettings(this);
    }

    public void testPermission(View view) {
        PermissionUtils.requestPermissionsResult(this, 10, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.i("ligen", "onPermissionGranted: 同意");
            }

            @Override
            public void onPermissionDenied(boolean neverRequest) {
                Log.i("ligen", "onPermissionGranted: 不同意" + neverRequest);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void testMMKVStorage(View view) {
        MMKVGetter.MMKV_IMPL().setString("key", "test");
    }

    public void getMMKVStorage(View view) {
        ((TextView) view).setText(MMKVGetter.MMKV_IMPL().getString("key", ""));
    }

    public void turnToKt(View view) {
        Intent intent = new Intent(MainActivity.this, ViewModelAct.class);
        startActivity(intent);
    }
}
