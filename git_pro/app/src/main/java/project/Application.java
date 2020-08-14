package project;

import android.content.Context;

import androidx.multidex.MultiDex;

import project.common.hook.HookManger;

public class Application extends android.app.Application {

    private static Application sApplication;

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
    }
}
