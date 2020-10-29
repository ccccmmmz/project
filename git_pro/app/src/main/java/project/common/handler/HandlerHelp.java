package project.common.handler;

import android.os.Looper;
import android.os.MessageQueue;

import java.lang.reflect.Field;

public class HandlerHelp {
    public static void addHanlerIdle() {
        Field field = null;
        try {
            field = Looper.class.getDeclaredField("mQueue");
            field.setAccessible(true);
            MessageQueue queue = (MessageQueue) field.get(Looper.getMainLooper());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
