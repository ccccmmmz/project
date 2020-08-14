package project.common.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 悬浮管理
 */
public class FloatManage {

    /**
     * TYPE_APPLICATION: 只能配合Activity在当前APP使用TYPE_APPLICATION_ATTACHED_DIALOG:
     * 只能配合Activity在当前APP使用
     * <p>
     * TYPE_CHANGED: 只能配合Activity在当前APP使用
     * <p>
     * 这种创建的view会被dialog遮挡
     *
     * @param context 宿主activity
     */
    //不需要权限的悬浮框


    static int x;
    static int y;
    static int nowX;
    static boolean move = false;
    static int startX;
    static int startY;

    @SuppressLint("ClickableViewAccessibility")
    public static void addNoNeedFloat(Context context) {


        Object systemService = context.getSystemService(Context.WINDOW_SERVICE);
        if (systemService instanceof WindowManager) {
            Button button = new Button(context);
            button.setText("不要权限的");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("不要悬浮点击了");
                }
            });

            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            params.width = -2;
            params.gravity = Gravity.START | Gravity.TOP;
            params.height = -2;
            params.format = PixelFormat.RGBA_8888;
            params.x = 0;
            params.y = 0;

            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            ((WindowManager) systemService).addView(button, params);

            button.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = x = (int) event.getRawX();
                            startY = y = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            nowX = (int) event.getRawX();
                            int nowY = (int) event.getRawY();
                            int movedX = nowX - x;
                            int movedY = nowY - y;
                            x = nowX;
                            y = nowY;
                            params.x = params.x + movedX;
                            params.y = params.y + movedY;
                            ((WindowManager) systemService).updateViewLayout(button, params);
                            if (x - startX > 20 || y - startY > 20 || x - startX < -20 || y - startY < -20)
                                move = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            int statX = (int) event.getRawX();
                            int startY = (int) event.getRawY();

                            if ((statX == x && startY == y) && !move) {
                                button.performClick();
                            }
                            move = false;
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        }
    }

}
