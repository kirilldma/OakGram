package org.telegram.margelet;

import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;

public class MargeletTheme {
    private static final int PINK_ACCENT_ID = 5;

    public static void applyOnFirstLaunch() {
        try {
            if (!MargeletConfig.claimFirstLaunch()) return;
            Theme.ThemeInfo night = Theme.getTheme("Night");
            if (night == null) return;
            night.setCurrentAccentId(PINK_ACCENT_ID);
            Theme.saveThemeAccents(night, true, false, true, false);
            Theme.applyTheme(night);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }
}
