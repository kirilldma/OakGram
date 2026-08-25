package org.telegram.margelet;

public class OakGhost {
    public static boolean shouldSuppressTyping() { return MargeletConfig.ghostNoTyping(); }
    public static boolean shouldSuppressStoryRead() { return MargeletConfig.ghostStealthStories(); }
    public static boolean shouldSuppressMessageRead() { return MargeletConfig.ghostStealthRead(); }
}
