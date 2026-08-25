package org.telegram.margelet;

import org.telegram.messenger.FileLog;
import java.io.*;

public class OakMetadata {

    public static boolean stripJpeg(File src, File dst) {
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
            int b1 = in.read(), b2 = in.read();
            if (b1 != 0xFF || b2 != 0xD8) return copy(src, dst);
            out.write(0xFF); out.write(0xD8);
            byte[] buf = new byte[8192];
            while (true) {
                int m1 = in.read();
                if (m1 == -1) break;
                if (m1 != 0xFF) { out.write(m1); continue; }
                int m2 = in.read();
                if (m2 == -1) break;
                if (m2 == 0xDA) {
                    out.write(0xFF); out.write(0xDA);
                    int r; while ((r = in.read(buf)) > 0) out.write(buf, 0, r);
                    break;
                }
                if (m2 == 0xD8 || (m2 >= 0xD0 && m2 <= 0xD7) || m2 == 0xD9 || m2 == 0x00) {
                    out.write(0xFF); out.write(m2);
                    continue;
                }
                int lenH = in.read(), lenL = in.read();
                if (lenH == -1 || lenL == -1) break;
                int len = ((lenH << 8) | lenL) - 2;
                if (m2 == 0xE1 || m2 == 0xED || m2 == 0xFE) {
                    long sk = 0;
                    while (sk < len) {
                        long s = in.skip(len - sk);
                        if (s <= 0) { if (in.read() == -1) break; sk++; } else sk += s;
                    }
                } else {
                    out.write(0xFF); out.write(m2); out.write(lenH); out.write(lenL);
                    int left = len;
                    while (left > 0) {
                        int r = in.read(buf, 0, Math.min(buf.length, left));
                        if (r <= 0) break;
                        out.write(buf, 0, r);
                        left -= r;
                    }
                }
            }
            return true;
        } catch (Throwable t) {
            FileLog.e(t);
            return copy(src, dst);
        }
    }

    public static File prepareOutgoingFile(File file) {
        if (file == null || !file.exists() || !MargeletConfig.stripExif()) return file;
        String name = file.getName().toLowerCase();
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            File target = new File(file.getParentFile(), "oak_" + file.getName());
            if (stripJpeg(file, target)) return target;
        }
        return file;
    }

    private static boolean copy(File src, File dst) {
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[16384];
            int r; while ((r = in.read(buf)) > 0) out.write(buf, 0, r);
            return true;
        } catch (Throwable t) { return false; }
    }
}
