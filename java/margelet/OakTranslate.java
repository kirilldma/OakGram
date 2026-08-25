package org.telegram.margelet;

import org.telegram.messenger.FileLog;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OakTranslate {
    public interface Callback {
        void onResult(String translated);
    }

    public static void translate(String text, String targetLang, Callback cb) {
        if (text == null || text.isEmpty()) {
            if (cb != null) cb.onResult(text);
            return;
        }
        new Thread(() -> {
            try {
                String tl = (targetLang != null && !targetLang.isEmpty()) ? targetLang : MargeletConfig.targetTranslateLang();
                String u = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + tl + "&dt=t&q=" + URLEncoder.encode(text, "UTF-8");
                HttpURLConnection conn = (HttpURLConnection) new URL(u).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                if (conn.getResponseCode() == 200) {
                    try (InputStream in = conn.getInputStream()) {
                        byte[] b = new byte[8192];
                        int r = in.read(b);
                        String raw = new String(b, 0, r, "UTF-8");
                        StringBuilder sb = new StringBuilder();
                        int pos = 0;
                        while ((pos = raw.indexOf("[\"", pos)) != -1) {
                            int end = raw.indexOf("\",\"", pos + 2);
                            if (end != -1) {
                                sb.append(raw.substring(pos + 2, end));
                                pos = end + 3;
                            } else break;
                        }
                        String res = sb.length() > 0 ? sb.toString() : text;
                        if (cb != null) cb.onResult(res);
                        return;
                    }
                }
            } catch (Throwable t) {
                FileLog.e(t);
            }
            if (cb != null) cb.onResult(text);
        }).start();
    }
}
