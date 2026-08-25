package org.telegram.margelet;

import android.content.Context;
import org.telegram.messenger.FileLog;
import java.io.File;

public class OakCacheCleaner {
    public static void cleanTempCache(Context ctx) {
        if (ctx == null) return;
        new Thread(() -> {
            try {
                del(ctx.getCacheDir());
                del(ctx.getExternalCacheDir());
                del(ctx.getCodeCacheDir());
            } catch (Throwable t) {
                FileLog.e(t);
            }
        }).start();
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
