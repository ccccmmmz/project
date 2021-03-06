package com.test.commonporject;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.common.permission.PermissionUtils;
import com.test.commonporject.test.ApiService;
import com.test.commonporject.test.BinderServer;
import com.test.commonporject.test.IServer;
import com.test.commonporject.test.TestStub;
import com.test.commonporject.vmtest.InputAct;

import java.lang.reflect.Field;
import java.util.List;

import project.common.DbUtils.BaseDaoImpl;
import project.common.behavior.ScrollingActivity;
import project.common.hook.HookManger;
import project.common.http.http.ApiDisposableObserver;
import project.common.http.http.ResponseThrowable;
import project.common.http.util.ApiKit;
import project.common.http.util.Utils;
import project.common.mmkv.MMKVGetter;
import project.common.system.FloatManage;
import project.common.test.OrderBean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BaseDaoImpl<OrderBean> dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this);
        test();
        HookManger.getInstance().hookStartActivity();

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
//        for (int i = 0; i < 100; i++) {
//            OrderBean userBean = new OrderBean(i, "地" + i);
//            dao.insert(userBean);
//        }
        goScroll();
    }

    private void goScroll() {
        startActivity(new Intent(this, ScrollingActivity.class));
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
        //OrderBean bean = new OrderBean(10, "修改10111");
        //dao.update(bean);

        try {
            //getClass().getSuperclass()
            Field mToken = getClass().getDeclaredField("mToken");
            Object o = mToken.get(this);
            if (o instanceof IBinder) {
                System.out.println("获取成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //app信息
    public void appInfo(View view) {
        //PermissionUtils.startAppSettings(this);
        // FloatManage.addNoNeedFloat(MainActivity.this);
        FloatManage.showAlertView(this);
        Dialog dialog = new Dialog(this);
        Button button = new Button(this);
        button.setText("挡住你");
        dialog.setContentView(button);
        Window window = dialog.getWindow();
        window.setDimAmount(0);
        window.setGravity(Gravity.CENTER);
        dialog.show();


        //boolean backgroundStart = PermissionUtils.canBackgroundStart(this);
        //if (!backgroundStart) {
        //    PermissionUtils.miuiPermission(this);
        //} else {
        //    Log.i("ligen", "appInfo: 允许后台启动");
        //}
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void testPermission(View view) {
        //PermissionUtils.requestPermissionsResult(this, 10, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
        //    @Override
        //    public void onPermissionGranted() {
        //        Log.i("ligen", "onPermissionGranted: 同意");
        //    }
        //
        //    @Override
        //    public void onPermissionDenied(boolean neverRequest) {
        //        Log.i("ligen", "onPermissionGranted: 不同意" + neverRequest);
        //    }
        //});
        PermissionUtils.askOverFlowWindow(this, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(boolean neverRequest) {

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
        Intent intent = new Intent(this, InputAct.class);
        startActivity(intent);
//        launchTest();
    }

    private void launchTest() {
        Uri uri = Uri.parse("app://");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void testIbinder(View view) {
        Intent intent = new Intent(this, BinderServer.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IServer iServer = TestStub.asInterface(service);
                iServer.test("哈哈");
                try {
                    service.linkToDeath(new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            Log.i(TAG, "binderDied: ");
                        }
                    }, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
    }
}
