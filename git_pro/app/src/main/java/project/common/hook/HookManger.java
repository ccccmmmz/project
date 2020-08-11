package project.common.hook;

import android.app.ActivityManager;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookManger {

    private boolean DEBUG = true;

    private HookManger() {

    }

    private static class HookMangerHolder {
        private static HookManger INSTANCE = new HookManger();
    }

    public static HookManger getInstance() {
        return HookMangerHolder.INSTANCE;
    }

    public void log(String info) {
        if (DEBUG) {
            System.out.println(info);
        }
    }

    public void hookStartActivity() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            hookStartActivity28Impl();
            return;
        }
        try {
            // 还原 gDefault() 成员
            Class activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            // 获取到成员变量
            Field gDefault = activityManagerNative.getDeclaredField("gDefault");
            gDefault.setAccessible(true);
            // 获取类成员变量，直接传空，因为是静态变量，所以获取到的是系统值,
            // 得到Singleton 静态类，
            Object defaultValue = gDefault.get(null);
            //mInstance对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstance = singletonClass.getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            // 获取到成员变量
            Object iActivityManagerObject = mInstance.get(defaultValue);
            log("获取对象成功");

            Class iActivityManagerIntercept = Class.forName("android.app.IActivityManager");
            StartActivityDelegate startActivity = new StartActivityDelegate(iActivityManagerObject);
            Object oldIActivityManager = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManagerIntercept},
                    startActivity
            );

            mInstance.set(defaultValue, oldIActivityManager);
            log("代理成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hookStartActivity28Impl() {
        Field field = null;
        try {
            field = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
            field.setAccessible(true);
            Object defaultValue = field.get(null);
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field instance = singletonClass.getDeclaredField("mInstance");
            instance.setAccessible(true);
            Object iActivityManagerObject = instance.get(defaultValue);
            Class iActivityManagerIntercept = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerIntercept}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    System.out.println(" P hook到的启动方法 " + method.getName());
                    return method.invoke(iActivityManagerObject, args);
                }
            });
            instance.set(defaultValue, proxy);
            log("p hook 成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dispatchStartActMethod(Method method) {
        switch (method.getName()) {
            case "startActivity":
                break;
        }
    }
}
