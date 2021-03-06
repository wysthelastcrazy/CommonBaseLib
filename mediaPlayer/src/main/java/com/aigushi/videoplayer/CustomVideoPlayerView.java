package com.aigushi.videoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
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
 * Created by yas on 2020-03-09
 * Describe:视频播放器，基于exoplayer2
 */
public class CustomVideoPlayerView extends FrameLayout {
    private final String TAG = "CustomVideoPlayerView";
    private PlayerView mPlayerView;
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
    public CustomVideoPlayerView(@NonNull Context context) {
        this(context,null);
    }

    public CustomVideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomVideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        {
            mPlayerView = new PlayerView(getContext());
            LayoutParams playerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mPlayerView.setLayoutParams(playerParams);
            mPlayerView.setOnTouchListener(playerTouchListener);
            mPlayerView.setUseController(false);
            addView(mPlayerView);

            /**创建DefaultTrackSelector对象，即磁道选择工厂对象*/
            //创建带宽对象
            BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
            //根据当前宽带来创建选择磁道工厂对象
            TrackSelection.Factory factory=new AdaptiveTrackSelection.Factory(bandwidthMeter);
            //传入工程对象，以便创建选择磁道工对象
            mTrackSelector=new DefaultTrackSelector(factory);

            /**创建播放器对象，并与View进行绑定*/
            //根据选择磁道创建播放器对象
            mPlayer= ExoPlayerFactory.newSimpleInstance(getContext(),mTrackSelector);
            mPlayer.addListener(eventListener);
            //将player和View绑定
            mPlayerView.setPlayer(mPlayer);
            mediaDataSourceFactory=new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(),getContext().getApplicationInfo().name));
        }
    }

    /**
     * 加载资源
     * @param url
     */
    public void prepare(String url){
        prepare(url,false);
    }
    public void prepare(String url,boolean autoPlay){
        String type = getUrlType(url);
        Log.d(TAG,"[play] type:"+type);
        //创建数据源
        MediaSource mediaSource =null;
        if ("m3u8".equals(type)) {
            //创建数据源
            if (cachedDataSourceFactory!=null){
                mediaSource = new HlsMediaSource.Factory(cachedDataSourceFactory)
                        .createMediaSource(Uri.parse(url));
            }else {
                mediaSource = new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(Uri.parse(url));
            }
        }else {
            if (cachedDataSourceFactory!=null) {
                mediaSource = new ExtractorMediaSource.Factory(cachedDataSourceFactory)
                        .createMediaSource(Uri.parse(url));
            }else{
                mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(Uri.parse(url));
            }
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

    private OnTouchListener playerTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };
    private String getUrlType(String url){
        if (!TextUtils.isEmpty(url)){
            String urlStr = url.split("\\?")[0];
            String[] params = urlStr.split("/");
            if (params.length>0) {
                String type = params[params.length - 1];
                if (!TextUtils.isEmpty(type)) {
                    String[] strs = type.split("\\.");
                    if (strs.length>1){
                        return strs[strs.length-1];
                    }
                }
            }
        }
        return null;
    }
}
