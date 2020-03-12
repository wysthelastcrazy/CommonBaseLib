package com.easefun.m3u8;

/**
 * Created by yas on 2020-02-13
 * Describe:
 */
public interface IDownloadListener {
    void onNewTaskAdd(M3U8DownloadRecord record);
    void onStart(M3U8DownloadRecord record);
    void onProgress(M3U8DownloadRecord record);
    void onPaused(M3U8DownloadRecord record);
    void onFinish(M3U8DownloadRecord record);
    void onFailed(M3U8DownloadRecord record, String errMsg);
    void onEnqueue(M3U8DownloadRecord record);
}
