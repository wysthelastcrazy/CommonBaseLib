package com.wys.commonbaselib.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.aigushi.videoplayer.CustomAudioPlayer;
import com.aigushi.videoplayer.IMediaPlayListener;
import com.wys.commonbaselib.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * music播放前台服务
 */
public class MusicService extends Service {
    private final String TAG = "MusicService";
    public static final String ACTION_PLAY_PAUSE = "action_play_pause";
    public static final String ACTION_PLAY_NEXT = "action_play_next";
    public static final String ACTION_PLAY_PRE = "action_play_pre";

    private final int notifyId = 1;
    private final String notificationChannelID = "1";
    private final String notificationChannelName = "爱学习古诗";
    private CustomAudioPlayer mPlayer;
    private List<MusicInfo> musicInfoList = new ArrayList<>();
    private int currMusicId;
    private int currPosition;

    private RemoteViews remoteView;
    private Notification notification;

    private ArrayList<WeakReference<IGSMusicPlayListener>> listeners = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PLAY_PAUSE);
        intentFilter.addAction(ACTION_PLAY_NEXT);
        intentFilter.addAction(ACTION_PLAY_PRE);
        registerReceiver(mReceiver, intentFilter);
        mPlayer = new CustomAudioPlayer(this);
        mPlayer.addPlayListener(mediaPlayListener);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"[onStartCommand]++++++++");
        initNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化通知栏
     */
    private void initNotification(){
        Notification.Builder builder = new Notification.Builder(this);

        Intent intent = new Intent(this,MusicListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        remoteView = new RemoteViews(getPackageName(), R.layout.music_notification);

        {
            remoteView.setOnClickPendingIntent(R.id.play_pause,getPendingIntent(this,ACTION_PLAY_PAUSE));
            remoteView.setOnClickPendingIntent(R.id.prev_song,getPendingIntent(this,ACTION_PLAY_PRE));
            remoteView.setOnClickPendingIntent(R.id.next_song,getPendingIntent(this,ACTION_PLAY_NEXT));
            String title = musicInfoList.get(currPosition).musicName;
            remoteView.setTextViewText(R.id.notification_title,title);

            if (mPlayer!=null&&mPlayer.isPlaying()){
                remoteView.setTextViewText(R.id.play_pause, "暂停");
            }else{
                remoteView.setTextViewText(R.id.play_pause, "播放");
            }
        }

        builder.setContentIntent(pendingIntent)
                .setContent(remoteView)
                .setWhen(System.currentTimeMillis())
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_LIGHTS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(notificationChannelID,notificationChannelName,
                    NotificationManager.IMPORTANCE_MIN);
            channel.setDescription("notification description");
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            manager.createNotificationChannel(channel);
            builder.setChannelId(notificationChannelID);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        notification = builder.build();
        startForeground(notifyId,notification);
//        updateNotification();
    }

    /**
     * 更新通知栏UI
     */
    private void updateNotification(){
        Log.d(TAG,"[updateNotification]++++++++");
        Log.d(TAG,"[updateNotification]  currPosition:"+currPosition);
        if (remoteView==null){
            return;
        }
        String title = musicInfoList.get(currPosition).musicName;
        remoteView.setTextViewText(R.id.notification_title,title);

        if (mPlayer.isPlaying()){
            remoteView.setTextViewText(R.id.play_pause, "暂停");
        }else{
            remoteView.setTextViewText(R.id.play_pause, "播放");
        }
        NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(notifyId,notification);
    }

    private PendingIntent getPendingIntent(Context context,String action){
        Intent intent = new Intent();
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public class MusicBinder extends Binder{
        public void setMusicList(List<MusicInfo> musicList){
            musicInfoList = musicList;
        }
        public void prepare(int position){
            prepareSource(position);
        }
        public boolean playPre(){
           return changeMusic(-1);
        }
        public boolean playNext(){
            boolean flag = changeMusic(1);
            if (!flag){
                notifyMusicListEnd();
            }
            return flag;
        }
        public void pause(){
            mPlayer.pause();
            notifyPause();
            updateNotification();
        }
        public void play(){
            mPlayer.start();
            notifyPlay();
            updateNotification();
        }
        public MusicInfo getCurrMusicInfo(){
            if (currPosition>=0&&currPosition<musicInfoList.size()){
                return musicInfoList.get(currPosition);
            }
            return null;
        }

        /**==================监听=====================**/

        public synchronized void register(IGSMusicPlayListener listener){
            if (listener == null) return;
            for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
                if (weakReference.get() == listener){
                    return;
                }
            }
            listeners.add(new WeakReference<IGSMusicPlayListener>(listener));
        }

        public synchronized void unRegister(IGSMusicPlayListener listener){
            if (listener == null) return;
            for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
                if (weakReference.get() == listener){
                    listeners.remove(weakReference);
                    return;
                }
            }
        }
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_PLAY_PAUSE.equals(action)){
                playOrPause();
            }else if (ACTION_PLAY_NEXT.equals(action)){
                changeMusic(1);
            }else if (ACTION_PLAY_PRE.equals(action)){
                changeMusic(-1);
            }
        }
    };

    /**
     * 暂停或取消暂停
     */
    private void playOrPause() {
        if (mPlayer!=null&&mPlayer.isPlaying()) {
            mPlayer.pause();
            notifyPause();
        } else {
            mPlayer.start();
            notifyPlay();
        }
        updateNotification();
    }
    /**
     * 切换music
     * @param type 1：下一首 -1：上一首
     * @return 是否切换成功
     */
    private boolean changeMusic(int type){
        int targetPos = currPosition+type;
        if (targetPos>=0&&targetPos<musicInfoList.size()){
            prepareSource(targetPos);
            return true;
        }
        return false;
    }

    /**
     * 加载资源
     * @param position 加载音频所在下标
     */
    private void prepareSource(int position){
        if (position>=0&&position<musicInfoList.size()){
            mPlayer.prepare(musicInfoList.get(position).url,true);
            notifyGetPoetryInfo(musicInfoList.get(position));
            currMusicId = musicInfoList.get(position).musicId;
            currPosition = position;
            updateNotification();
        }
    }


    /**
     * 音频播放器状态监听
     */
    private IMediaPlayListener mediaPlayListener = new IMediaPlayListener() {
        @Override
        public void onShowBuffering() {
            notifyShowBuffering();
        }

        @Override
        public void onHideBuffering() {
            notifyHideBuffering();
        }

        @Override
        public void onReady(long duration) {
            notifyReady(duration);
        }

        @Override
        public void onUpdateProgress(long currentPosition, long bufferedPosition) {
            notifyUpdateProgress(currentPosition,bufferedPosition);
        }

        @Override
        public void onPlayFinished() {
            notifyPlayEnd();
        }

        @Override
        public void onPlayError(int code, String errorMsg) {
            notifyPlayError(code,errorMsg);
        }
    };

    /**
     * 播放中卡顿缓冲
     */
    private void notifyShowBuffering(){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onShowBuffering();
        }
    }
    private void notifyHideBuffering(){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onHideBuffering();
        }
    }

    private void notifyGetPoetryInfo(MusicInfo infoBean){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onGetPoetryInfoCallback(infoBean);
        }
    }
    private void notifyReady(long duration){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onAudioReady(duration);
        }
    }

    private void notifyPlay(){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onAudioPlay();
        }
    }
    private void notifyPause(){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onAudioPause();
        }
    }
    private void notifyUpdateProgress(long currentPosition, long bufferedPosition){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onAudioUpdateProgress(currentPosition, bufferedPosition);
        }
    }
    private void notifyPlayEnd(){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onAudioEnd();
        }
    }

    private void notifyPlayError(int code, String errorMsg){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onAudioError(code,errorMsg);
        }
    }
    private void notifyMusicListEnd(){
        for (WeakReference<IGSMusicPlayListener> weakReference : listeners){
            weakReference.get().onMusicListEnd();
        }
    }
}
