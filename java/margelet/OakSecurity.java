package org.telegram.margelet;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class OakSecurity {
    public static void applyWindowFlags(Activity act) {
        if (act == null) return;
        Window w = act.getWindow();
        if (w == null) return;
        if (MargeletConfig.blockScreenshots()) {
            w.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            w.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    public static boolean checkDuressPin(Activity act, String input) {
        if (MargeletConfig.isDuressPin(input)) {
            OakPanic.executePanicWipe(act);
            return true;
        }
        return false;
    }
}
