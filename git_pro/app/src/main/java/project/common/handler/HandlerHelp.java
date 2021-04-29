package project.common.handler;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;

import java.lang.reflect.Field;

public class HandlerHelp {
    private static final String TAG = "HandlerHelp";
    public static void addHanlerIdle() {
        Field field = null;
        try {
            field = Looper.class.getDeclaredField("mQueue");
            field.setAccessible(true);
            MessageQueue queue = (MessageQueue) field.get(Looper.getMainLooper());
            queue.addIdleHandler(() -> {
                Log.e(TAG, "queueIdle:  enter");
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
