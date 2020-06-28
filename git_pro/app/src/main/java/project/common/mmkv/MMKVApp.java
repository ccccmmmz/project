package project.common.mmkv;

import android.app.Application;

import com.tencent.mmkv.MMKV;

/**
 * description：
 * ===============================
 * creator：zhenqiang
 * create time：2020-04-21  17:08
 * ===============================
 * reasons for modification：
 * Modifier：
 * Modify time：2020-04-21  17:08
 */
public class MMKVApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String dir = getFilesDir().getAbsolutePath() + "/mmkv";
        MMKV.initialize(dir);
    }
}
