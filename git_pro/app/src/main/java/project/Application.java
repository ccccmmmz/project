package project;

import com.tencent.mmkv.MMKV;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String dir = getFilesDir().getAbsolutePath() + "/mmkv";
        MMKV.initialize(dir);
    }
}
