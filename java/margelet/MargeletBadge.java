package org.telegram.margelet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import org.telegram.messenger.AndroidUtilities;

public class MargeletBadge {

    public static class Badge {
        public final long peerId;
        public final String title;
        public final String about;
        public final int color;
        public final String url;

        public Badge(long peerId, String title, String about, int color, String url) {
            this.peerId = peerId;
            this.title = title;
            this.about = about;
            this.color = color;
            this.url = url;
        }

        public String title() { return title != null ? title : ""; }
        public String about() { return about != null ? about : ""; }
    }

    private static final Badge[] BUILT_IN = {
        new Badge(-4426743212L, "OakGram", "Official OakGram Channel", 0xFFFF4081, MargeletConfig.CHANNEL_URL),
        new Badge(-4436273526L, "OakGram Community", "Official OakGram Forum", 0xFFFF4081, MargeletConfig.FORUM_URL),
    };

    public static void refresh() {}

    public static Badge of(long peerId) {
        if (!MargeletConfig.badgesEnabled()) return null;
        for (Badge b : BUILT_IN) {
            if (b.peerId == peerId) return b;
        }
        return null;
    }

    public static Badge[] list() {
        return BUILT_IN;
    }

    public static Drawable iconDrawable(Context context, Badge badge) {
        if (badge == null) return null;
        try {
            GradientDrawable field = new GradientDrawable();
            field.setShape(GradientDrawable.RECTANGLE);
            field.setCornerRadius(AndroidUtilities.dp(24) * 5f / 24f);
            field.setColor(badge.color);
            return field;
        } catch (Throwable t) {
            return null;
        }
    }

    public static String title(long peerId) {
        Badge b = of(peerId);
        return b != null ? b.title() : null;
    }

    public static void show(Context context, long peerId) {}
}
