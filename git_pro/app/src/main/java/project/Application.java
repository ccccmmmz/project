package project;

import android.content.Context;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //String dir = getFilesDir().getAbsolutePath() + "/mmkv";
        //MMKV.initialize(dir, new MMKV.LibLoader() {
        //    @Override
        //    public void loadLibrary(String s) {
        //        ReLinker.loadLibrary(Application.this, s);
        //    }
        //});


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}
