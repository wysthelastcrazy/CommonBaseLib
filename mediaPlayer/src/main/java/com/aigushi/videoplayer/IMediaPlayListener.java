package com.aigushi.videoplayer;

/**
 * Created by yas on 2019/8/20
 * Describe:
 */
public interface IMediaPlayListener {
    /**
     * 展示加载loading
     */
    void onShowBuffering();
    /**
     *  隐藏加载loading
     */
    void onHideBuffering();
    /**
     * 资源准备就绪
     */
    void onReady(long duration);
    /**
     *
     * @param currentPosition
     * @param bufferedPosition
     */
    void onUpdateProgress(long currentPosition,long bufferedPosition);
    /**
     * 播放完成
     */
    void onPlayFinished();
    /**
     *  播放错误
     */
    void onPlayError(int code, String errorMsg);
}
