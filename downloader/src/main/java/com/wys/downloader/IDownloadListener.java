package com.wys.downloader;

/**
 * Created by yas on 2020-02-13
 * Describe:
 */
public interface IDownloadListener {
    void onNewTaskAdd(DownloadRecord record);
    void onStart(DownloadRecord record);
    void onProgress(DownloadRecord record);
    void onPaused(DownloadRecord record);
    void onFinish(DownloadRecord record);
    void onFailed(DownloadRecord record, String errMsg);
    void onEnqueue(DownloadRecord record);
}
