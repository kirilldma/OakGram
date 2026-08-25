package org.telegram.margelet;

import android.content.Context;
import android.content.SharedPreferences;
import org.telegram.messenger.ApplicationLoader;

public class OakPluginGuard {
    private static SharedPreferences prefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences("oak_plugin_firewall", Context.MODE_PRIVATE);
    }
    public static boolean isNetworkAllowed(String id) {
        return !MargeletConfig.pluginFirewallEnabled() || prefs().getBoolean("net_" + id, false);
    }
    public static void setNetworkAllowed(String id, boolean ok) {
        prefs().edit().putBoolean("net_" + id, ok).apply();
    }
    public static boolean isSendAllowed(String id) {
        return !MargeletConfig.pluginFirewallEnabled() || prefs().getBoolean("send_" + id, true);
    }
    public static void setSendAllowed(String id, boolean ok) {
        prefs().edit().putBoolean("send_" + id, ok).apply();
    }
}
