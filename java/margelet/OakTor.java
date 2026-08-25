package org.telegram.margelet;

import org.telegram.messenger.FileLog;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class OakTor {
    public static final String TOR_HOST = "127.0.0.1";
    public static final int TOR_PORT = 9050;

    public static void apply(boolean enable) {
        MargeletConfig.setUseTor(enable);
        try {
            Class<?> sharedConfigClass = Class.forName("org.telegram.messenger.SharedConfig");
            Class<?> proxyInfoClass = Class.forName("org.telegram.messenger.SharedConfig$ProxyInfo");
            
            if (enable) {
                Constructor<?> ctor = null;
                for (Constructor<?> c : proxyInfoClass.getDeclaredConstructors()) {
                    Class<?>[] pts = c.getParameterTypes();
                    if (pts.length >= 5 && pts[0] == String.class && pts[1] == int.class) {
                        ctor = c;
                        break;
                    }
                }
                if (ctor != null) {
                    Object proxyObj = null;
                    if (ctor.getParameterTypes().length == 5) {
                        proxyObj = ctor.newInstance(TOR_HOST, TOR_PORT, "", "", "");
                    } else if (ctor.getParameterTypes().length == 6) {
                        proxyObj = ctor.newInstance(TOR_HOST, TOR_PORT, "", "", "", "");
                    }
                    if (proxyObj != null) {
                        Method addProxy = sharedConfigClass.getMethod("addProxy", proxyInfoClass);
                        addProxy.invoke(null, proxyObj);
                        Field currentProxy = sharedConfigClass.getField("currentProxy");
                        currentProxy.set(null, proxyObj);
                    }
                }
                try {
                    Field proxyEnable = sharedConfigClass.getField("isProxyEnabled");
                    proxyEnable.setBoolean(null, true);
                } catch (Throwable ignored) {}
            }
            try {
                Method save = sharedConfigClass.getMethod("saveProxyList");
                save.invoke(null);
            } catch (Throwable ignored) {}
        } catch (Throwable t) {
            FileLog.e(t);
        }
    }
}
