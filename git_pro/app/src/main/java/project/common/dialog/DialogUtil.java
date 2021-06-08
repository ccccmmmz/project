package project.common.dialog;

import android.app.Activity;
import android.os.IBinder;
import android.view.Window;
import android.view.WindowManager;

public class DialogUtil {

    //获取dialog依赖的token
    private IBinder getSubWindow(Activity context) {
        if (context == null) {
            return null;
        }
        Window window = context.getWindow();
        if (window == null) {
            return null;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        return attributes.token;
    }

}
