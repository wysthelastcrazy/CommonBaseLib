package com.aigushi.videoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

/**
 * Created by yas on 2020-03-10
 * Describe:音频播放器
 */
public class CustomAudioPlayer {
    private TrackSelector mTrackSelector;
    private SimpleExoPlayer mPlayer;
    //定义数据源工厂对象
    DataSource.Factory mediaDataSourceFactory;
    private SimpleCache simpleCache;
    private CacheDataSourceFactory cachedDataSourceFactory;
    private IMediaPlayListener mediaPlayListener;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x100:
                    updateProgress();
                    break;
            }
        }
    };

    public CustomAudioPlayer(Context context){
        init(context);
    }
    private void init(Context context){
        /**创建DefaultTrackSelector对象，即磁道选择工厂对象*/
        //创建带宽对象
        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
        //根据当前宽带来创建选择磁道工厂对象
        TrackSelection.Factory factory=new AdaptiveTrackSelection.Factory(bandwidthMeter);
        //传入工程对象，以便创建选择磁道工对象
        mTrackSelector=new DefaultTrackSelector(factory);

        /**创建播放器对象，并与View进行绑定*/
        //根据选择磁道创建播放器对象
        mPlayer= ExoPlayerFactory.newSimpleInstance(context,mTrackSelector);
        mPlayer.addListener(eventListener);
        mediaDataSourceFactory=new DefaultDataSourceFactory(context,
                Util.getUserAgent(context,context.getApplicationInfo().name));
    }
    /**
     * 加载资源
     * @param url
     */
    public void prepare(String url){
        prepare(url,false);
    }
    public void prepare(String url,boolean autoPlay){
        //创建数据源
        MediaSource mediaSource =null;
        if (cachedDataSourceFactory!=null) {
            mediaSource = new ExtractorMediaSource.Factory(cachedDataSourceFactory)
                    .createMediaSource(Uri.parse(url));
        }else{
            mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(Uri.parse(url));
        }
        isFirstPlay = true;
        isPlayFinish = false;
        //添加数据源到播放器中
        mPlayer.prepare(mediaSource);
        if (autoPlay){
            start();
        }
    }

    /**
     * 开始播放
     */
    public void start(){
        if (mPlayer!=null&&!mPlayer.getPlayWhenReady()){
            mPlayer.setPlayWhenReady(true);
            updateProgress();
        }
    }

    /**
     * 暂停
     */
    public void pause(){
        if (mPlayer!=null&&mPlayer.getPlayWhenReady()){
            mPlayer.setPlayWhenReady(false);
            mHandler.removeMessages(0x100);
        }
    }
    /**
     * 滑动音频进度
     * @param progress
     */
    public void seekTo(long progress) {
        if (mPlayer != null) {
            mPlayer.seekTo(progress);
            isPlayFinish = false;
        }
    }
    public boolean isPlaying(){
        if (mPlayer!=null){
            return mPlayer.getPlayWhenReady();
        }
        return false;
    }
    public void addPlayListener(IMediaPlayListener mediaPlayListener){
        this.mediaPlayListener = mediaPlayListener;
    }

    /**
     * 更新进度
     */
    private void updateProgress(){
        if (mediaPlayListener!=null&&mPlayer!=null) {
            mHandler.removeMessages(0x100);
            mediaPlayListener.onUpdateProgress(mPlayer.getCurrentPosition(),
                    mPlayer.getBufferedPosition());
            mHandler.sendEmptyMessageDelayed(0x100, 1000);
        }
    }

    /**
     * 设置缓存，非必须
     * @param cachePath
     * @param maxBytes
     */
    public void setCache(String cachePath,long maxBytes){
        if (TextUtils.isEmpty(cachePath)||maxBytes<=0){
            return;
        }
        if (simpleCache!=null||cachedDataSourceFactory!=null){
            return;
        }
        File cacheFile = new File(cachePath);
        simpleCache = new SimpleCache(cacheFile,
                new LeastRecentlyUsedCacheEvictor(maxBytes));
        cachedDataSourceFactory = new CacheDataSourceFactory(simpleCache,
                mediaDataSourceFactory);
    }
    public void release(){
        if (mPlayer!=null){
            mPlayer.setPlayWhenReady(false);
            mPlayer.release();
        }
        if (simpleCache!=null){
            try {
                simpleCache.disableCacheFolderLocking();
                simpleCache.release();
            } catch (Cache.CacheException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isFirstPlay = true;     //第一次播放标示，防止多次触犯开始事件
    private boolean isPlayFinish = false;   //播放完成标示，防止多次触发结束事件
    private Player.DefaultEventListener eventListener = new Player.DefaultEventListener() {

        @Override
        public void onLoadingChanged(boolean isLoading) {
            if (mediaPlayListener==null)return;
            if (isLoading){
                mediaPlayListener.onShowBuffering();
            }else{
                mediaPlayListener.onHideBuffering();
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY&&isFirstPlay&&mPlayer.getDuration()>0){
                if (mediaPlayListener!=null)
                    mediaPlayListener.onReady(mPlayer.getDuration());
                isFirstPlay = false;
            }else if (playbackState == Player.STATE_ENDED&&!isPlayFinish){
                if (mediaPlayListener!=null)
                    mediaPlayListener.onPlayFinished();
                mPlayer.setPlayWhenReady(false);
                isPlayFinish = true;
            }
        }
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (mediaPlayListener!=null){
                mediaPlayListener.onPlayError(-100,error.toString());
            }
        }
    };
}
