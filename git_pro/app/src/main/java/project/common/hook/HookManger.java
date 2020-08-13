package project.common.hook;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

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
            case "handleApplicationCrash":
                break;
            case "reportKillProcessEvent":
                break;
        }
    }


    //这个9.0前后没啥区别
    public void hookInstrumentation() {

        try {
            // 先获取到当前的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            //拿到在ActivityThread类里面的原始mInstrumentation对象
            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);
            //构建我们的代理对象
            Instrumentation evilInstrumentation = new InstrumentationProxy(mInstrumentation);
            mInstrumentationField.set(currentActivityThread, evilInstrumentation);
            System.out.println("hookInstrumentation success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class InstrumentationProxy extends Instrumentation {
        public static final String TAG = "InstrumentationProxy";
        public static final String EXEC_START_ACTIVITY = "execStartActivity";

        public Instrumentation oldInstrumentation;

        public InstrumentationProxy(Instrumentation mInstrumentation) {
            oldInstrumentation = mInstrumentation;
        }

        public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                                Intent intent, int requestCode, Bundle options) {
            Log.d(TAG, "\n打印调用startActivity相关参数: \n" + "who = [" + who + "], " +
                    "\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "], " +
                    "\ntarget = [" + target + "], \nintent = [" + intent +
                    "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");


            Log.i(TAG, "------------hook  success------------->");
            Log.i(TAG, "这里可以做你在打开StartActivity方法之前的事情");
            Log.i(TAG, "------------hook  success------------->");
            Log.i(TAG, "");

            //由于这个方法是隐藏的，所以需要反射来调用，先找到这方法
            try {
                Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                        EXEC_START_ACTIVITY,
                        Context.class, IBinder.class, IBinder.class, Activity.class,
                        Intent.class, int.class, Bundle.class);
                execStartActivity.setAccessible(true);
                return (ActivityResult) execStartActivity.invoke(oldInstrumentation, who,
                        contextThread, token, target, intent, requestCode, options);
            } catch (Exception e) {
                //如果你在这个类的成员变量Instrumentation的实例写错mInstrument,代码讲会执行到这里来
                throw new RuntimeException("if Instrumentation paramerter is mInstrumentation, hook will fail");
            }
        }

    }
}
