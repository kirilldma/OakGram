package org.telegram.margelet;

public class OakMediaQuality {
    public static int getPhotoUploadMaxResolution(int defaultRes) {
        if (MargeletConfig.maxQualityPhotos()) return 2560;
        return defaultRes;
    }

    public static int getPhotoUploadQuality(int defaultQuality) {
        if (MargeletConfig.maxQualityPhotos()) return 98;
        return defaultQuality;
    }

    public static int getParallelDownloadChunks(int defaultChunks) {
        if (MargeletConfig.boostDownloads()) return Math.max(defaultChunks * 2, 8);
        return defaultChunks;
    }
}
