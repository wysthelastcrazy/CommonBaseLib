package com.wys.baselib.net.callback;

/**
 * Created by yas on 2019/6/24
 * Describe:
 */
public interface IDownloadCallback {
    void onStart();
    void onProgress(long currProgress,long total);
    void onComplete();
    void onError(String errorMsg);
}
