package project;

import android.content.Context;

import androidx.multidex.MultiDex;

public class Application extends android.app.Application {
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
    }
}
