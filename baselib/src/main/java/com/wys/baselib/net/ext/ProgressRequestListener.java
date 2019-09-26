package com.wys.baselib.net.ext;

/**
 * Created by yas on 2019/7/1
 * Describe:
 */
public interface ProgressRequestListener {
    void onStart();
    void onProgress(long currProgress,long total);
    void onComplete();
    void onError(String errorMsg);
}
