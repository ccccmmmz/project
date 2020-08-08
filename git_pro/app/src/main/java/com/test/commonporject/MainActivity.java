package com.test.commonporject;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.common.permission.PermissionUtils;
import com.tencent.mmkv.MMKV;
import com.test.commonporject.test.ApiService;
import com.test.commonporject.vmtest.ViewModelAct;

import java.util.List;

import project.common.DbUtils.BaseDaoImpl;
import project.common.http.http.ApiDisposableObserver;
import project.common.http.http.ResponseThrowable;
import project.common.http.util.ApiKit;
import project.common.http.util.Utils;
import project.common.mmkv.MMKVGetter;
import project.common.test.OrderBean;

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

        MMKV.initialize(this);
    }

    private void test() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                i -> {
                    int height = decorView.getHeight();
                    Log.i("ligen", "Current height: " + height);
                });
    }

    public void testHttp() {
        ApiKit.execute(ApiKit.getInstance().create(ApiService.class).test1(), new ApiDisposableObserver<Object>() {
            @Override
            public void onResult(Object o) {
                System.out.println(o);
            }

            @Override
            public void onError(ResponseThrowable responseThrowable) {

            }
        });
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
        //PermissionUtils.startAppSettings(this);
        boolean backgroundStart = PermissionUtils.canBackgroundStart(this);
        if (!backgroundStart) {
            PermissionUtils.miuiPermission(this);
        } else {
            Log.i("ligen", "appInfo: 允许后台启动");
        }
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
