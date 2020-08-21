package project.common.hook;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class StartActivityDelegate implements InvocationHandler {
    private Object iActivityManager;

    public StartActivityDelegate(Object iActivityManager) {
        this.iActivityManager = iActivityManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            System.out.println("拦截到启动activity");
        }
        HookManger.getInstance().dispatchStartActMethod(method);
        return method.invoke(iActivityManager, args);
    }
}
