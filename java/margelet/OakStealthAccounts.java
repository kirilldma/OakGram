package org.telegram.margelet;

public class OakStealthAccounts {
    public static boolean isVisible(int accountId) {
        if (MargeletConfig.isStealthUnlocked()) return true;
        return !MargeletConfig.isAccountHidden(accountId);
    }

    public static boolean checkSearchQuery(String query) {
        if (query == null) return false;
        String pass = MargeletConfig.stealthAccountPasscode();
        if (!pass.isEmpty() && pass.equals(query.trim())) {
            MargeletConfig.setStealthUnlocked(!MargeletConfig.isStealthUnlocked());
            return true;
        }
        return false;
    }
}
