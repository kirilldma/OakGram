package org.telegram.margelet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OakLinkSanitizer {
    private static final Set<String> TRACKING = new HashSet<>(Arrays.asList(
        "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content", "utm_id",
        "fbclid", "gclid", "gclsrc", "dclid", "wbraid", "gbraid", "si", "igshid", "igsh",
        "ref", "ref_src", "ref_url", "_hsenc", "_hsmi", "mc_cid", "mc_eid", "yclid", "ymclid"
    ));
    private static final Pattern URL_PAT = Pattern.compile("(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)", Pattern.CASE_INSENSITIVE);

    public static String cleanUrl(String url) {
        if (url == null || !url.contains("?")) return url;
        try {
            int q = url.indexOf('?'), h = url.indexOf('#', q);
            String base = url.substring(0, q), query = h != -1 ? url.substring(q + 1, h) : url.substring(q + 1);
            String hash = h != -1 ? url.substring(h) : "";
            StringBuilder clean = new StringBuilder();
            for (String p : query.split("&")) {
                if (p.isEmpty()) continue;
                String k = p.split("=")[0].toLowerCase().trim();
                if (!TRACKING.contains(k)) clean.append(clean.length() > 0 ? "&" : "").append(p);
            }
            return base + (clean.length() > 0 ? "?" + clean : "") + hash;
        } catch (Throwable t) { return url; }
    }

    public static String sanitizeText(String text) {
        if (text == null || text.isEmpty() || !MargeletConfig.sanitizeLinks()) return text;
        Matcher m = URL_PAT.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) m.appendReplacement(sb, Matcher.quoteReplacement(cleanUrl(m.group(1))));
        return m.appendTail(sb).toString();
    }
}
