package project.common.enhance;

import android.os.StrictMode;

public class StrickModeKit {

    public static void initStrict() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()//开启所有的detectXX系列方法
                .penaltyDialog()//弹出违规提示框
                .penaltyLog()//在Logcat中打印违规日志
                .build());
    }
}
