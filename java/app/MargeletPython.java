package org.telegram.margelet;

import android.content.Context;
import org.telegram.messenger.FileLog;
import java.lang.reflect.Method;

public class MargeletPython {

    private static Object pythonInstance;
    private static Method getModuleMethod;
    private static Method callAttrMethod;

    public static void start(Context context) {
        try {
            Class<?> pyClass = Class.forName("com.chaquo.python.Python");
            Method isStarted = pyClass.getMethod("isStarted");
            if (!(Boolean) isStarted.invoke(null)) {
                Class<?> platformClass = Class.forName("com.chaquo.python.android.AndroidPlatform");
                Object platform = platformClass.getConstructor(Context.class).newInstance(context);
                Method startMethod = pyClass.getMethod("start", Class.forName("com.chaquo.python.PythonPlatform"));
                startMethod.invoke(null, platform);
            }
            Method getInstance = pyClass.getMethod("getInstance");
            pythonInstance = getInstance.invoke(null);
            getModuleMethod = pyClass.getMethod("getModule", String.class);
            Class<?> pyObjClass = Class.forName("com.chaquo.python.PyObject");
            callAttrMethod = pyObjClass.getMethod("callAttr", String.class, Object[].class);
        } catch (Throwable t) {
            FileLog.e(t);
        }
    }

    private static Object call(String func, Object... args) {
        try {
            if (pythonInstance == null || getModuleMethod == null || callAttrMethod == null) return null;
            Object host = getModuleMethod.invoke(pythonInstance, "margelet_host");
            if (host != null) {
                return callAttrMethod.invoke(host, func, args);
            }
        } catch (Throwable t) {
            FileLog.e(t);
        }
        return null;
    }

    public static void chatOpened(Object fragment) {
        call("chat_opened", fragment);
    }

    public static String sending(String text, long dialogId) {
        Object res = call("sending", text, dialogId);
        return res == null ? text : res.toString();
    }

    public static void received(String text, long dialogId, int messageId, boolean out) {
        call("received", text, dialogId, messageId, out);
    }

    public static void buttonClicked(String pluginId, String key, Object fragment) {
        call("button_clicked", pluginId, key, fragment);
    }

    public static void settingsChanged(String pluginId, String key, String value) {
        call("settings_changed", pluginId, key, value);
    }

    public static void run(String id, String name, String folder) {
        call("run_plugin", id, name, folder);
    }
}
