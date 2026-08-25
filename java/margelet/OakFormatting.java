package org.telegram.margelet;

public class OakFormatting {
    public static String wrapMonospace(String text) {
        if (text == null || text.isEmpty()) return text;
        return "`" + text.trim() + "`";
    }

    public static String wrapCodeBlock(String text, String lang) {
        if (text == null || text.isEmpty()) return text;
        String l = (lang != null && !lang.isEmpty()) ? lang.trim() : "";
        return "```" + l + "\n" + text + "\n```";
    }
}
