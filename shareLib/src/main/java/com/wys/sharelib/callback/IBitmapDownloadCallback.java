package com.wys.sharelib.callback;

import android.graphics.Bitmap;

/**
 * @author wangyasheng
 * @date 2020/9/27
 * @Describe:
 */
public interface IBitmapDownloadCallback {
    /**
     * bitmap下载成功
     * @param bitmap
     */
    void onSuccess(Bitmap bitmap);
}
