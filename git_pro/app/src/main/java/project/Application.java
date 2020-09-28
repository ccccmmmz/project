package project;

import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import project.common.hook.HookManger;
import project.common.system.SystemHelper;

public class Application extends android.app.Application {

    private static final String TAG = "Application";

    public static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void init() {
        //String dir = getFilesDir().getAbsolutePath() + "/mmkv";
        //MMKV.initialize(dir, s -> ReLinker.loadLibrary(Application.this, s));

        //MMKV.initialize(this);
        sApplication = this;
        HookManger.getInstance().hookInstrumentation();
        //HookManger.getInstance().hookTypeValue();

        //测试进程名
        testProgressName();
    }

    private void testProgressName() {
        Log.d(TAG, "testProgressName: " + SystemHelper.getProcessNameFeature(this));
    }
}
