package com.wys.commonbaselib.music;

/**
 * Created by yas on 2019/8/20
 * Describe:
 */
public interface IGSMusicPlayListener {
    /**缓冲*/
    void onShowBuffering();
    /**结束缓冲**/
    void onHideBuffering();
    /**预加载完成**/
    void onAudioReady(long duration);
    /**播放**/
    void onAudioPlay();
    /**暂停**/
    void onAudioPause();
    /**进度刷行*/
    void onAudioUpdateProgress(long currentPosition, long bufferedPosition);
    /**播放结束（单首）**/
    void onAudioEnd();
    /**播放错误**/
    void onAudioError(int code, String errorMsg);
    /**当前诗歌数据获取成功**/
    void onGetPoetryInfoCallback(MusicInfo infoBean);
    /**当前播放列表播放结束**/
    void onMusicListEnd();
}
