package org.telegram.margelet;

import org.telegram.messenger.FileLog;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OakDns {
    public static final String[] DOH_PROVIDERS = {
        "Выключен (Системный DNS)",
        "Mullvad DoH",
        "Quad9 DoH",
        "Cloudflare DoH",
        "AdGuard DoH"
    };

    public static final String[] DOH_URLS = {
        "",
        "https://dns.mullvad.net/dns-query",
        "https://dns.quad9.net/dns-query",
        "https://cloudflare-dns.com/dns-query",
        "https://dns.adguard-dns.com/dns-query"
    };

    public static List<InetAddress> resolve(String host) {
        List<InetAddress> result = new ArrayList<>();
        int p = MargeletConfig.dohProvider();
        if (p <= 0 || p >= DOH_URLS.length) {
            try {
                for (InetAddress a : InetAddress.getAllByName(host)) result.add(a);
            } catch (Throwable ignored) {}
            return result;
        }

        try {
            String queryUrl = DOH_URLS[p] + "?name=" + host + "&type=A";
            HttpURLConnection conn = (HttpURLConnection) new URL(queryUrl).openConnection();
            conn.setRequestProperty("Accept", "application/dns-json");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            if (conn.getResponseCode() == 200) {
                try (InputStream in = conn.getInputStream()) {
                    byte[] b = new byte[4096];
                    int r = in.read(b);
                    String resp = new String(b, 0, r);
                    int idx = 0;
                    while ((idx = resp.indexOf("\"data\":\"", idx)) != -1) {
                        int end = resp.indexOf("\"", idx + 8);
                        if (end != -1) {
                            String ip = resp.substring(idx + 8, end);
                            try { result.add(InetAddress.getByName(ip)); } catch (Throwable ignored) {}
                            idx = end + 1;
                        } else break;
                    }
                }
            }
        } catch (Throwable t) {
            FileLog.e(t);
            try {
                for (InetAddress a : InetAddress.getAllByName(host)) result.add(a);
            } catch (Throwable ignored) {}
        }
        return result;
    }
}
