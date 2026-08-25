package org.telegram.margelet;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import org.telegram.messenger.ApplicationLoader;
import java.io.File;

public class OakPanic {
    public static void executePanicWipe(Activity act) {
        try {
            Context ctx = ApplicationLoader.applicationContext != null ? ApplicationLoader.applicationContext : (act != null ? act.getApplicationContext() : null);
            if (ctx == null) return;
            del(ctx.getCacheDir());
            del(ctx.getExternalCacheDir());
            del(ctx.getCodeCacheDir());
            del(ctx.getFilesDir());
            del(new File(ctx.getApplicationInfo().dataDir, "shared_prefs"));
            del(new File(ctx.getApplicationInfo().dataDir, "databases"));
        } catch (Throwable ignored) {}
        if (act != null) act.finishAffinity();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    private static void del(File f) {
        if (f == null || !f.exists()) return;
        if (f.isDirectory()) {
            File[] list = f.listFiles();
            if (list != null) for (File c : list) del(c);
        }
        try { f.delete(); } catch (Throwable ignored) {}
    }
}
