package org.telegram.margelet;

public class OakPinning {
    public static int getMaxPinnedDialogsCount(int defaultCount) {
        if (MargeletConfig.unlimitedPins()) return 1000;
        return defaultCount;
    }
}
