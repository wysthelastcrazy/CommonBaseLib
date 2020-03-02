package com.wys.commonbaselib.im;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wys.commonbaselib.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yas on 2020-02-19
 * Describe:
 */
public class GslRecordPlayerActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_base_right;
    private GslRecMessageListView msgListView;
    private Button btn_put_message;
    private Button btn_put_messages;
    private Button btn_update_messages;

    private LinearLayout ll_trophy;

    private ArrayList<GslRecMessage> messages;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsl_record_player);

        initMessages();
        ll_base_right = findViewById(R.id.ll_base_right);
//        msgListView = findViewById(R.id.msgListView);
        msgListView = new GslRecMessageListView(this,ll_base_right,"002");
        msgListView.setRoomId("001");

        btn_put_message = findViewById(R.id.btn_put_message);
        btn_put_message.setOnClickListener(this);
        btn_put_messages = findViewById(R.id.btn_put_messages);
        btn_put_messages.setOnClickListener(this);

        btn_update_messages = findViewById(R.id.btn_update_messages);
        btn_update_messages.setOnClickListener(this);

        initTrophy();
        initSoundPool();
    }

    private void initTrophy(){
        ll_trophy = findViewById(R.id.ll_trophy);
        for (int i = 0 ; i<3; i++){
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.dp_73),
                    getResources().getDimensionPixelOffset(R.dimen.dp_55));
            params.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dp_24);
            img.setLayoutParams(params);
            img.setTag("tag"+i);
            img.setImageResource(R.mipmap.img_user_headpic);
            ll_trophy.addView(img);
        }
    }

    private void showTrophy(){
        ImageView img = new ImageView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.dp_60),
                getResources().getDimensionPixelOffset(R.dimen.dp_60));
        layoutParams.gravity = Gravity.CENTER;
        img.setLayoutParams(layoutParams);
        img.setImageResource(R.mipmap.ic_launcher);
        getRootView().addView(img);
        showAnim(img);
    }
    private void showAnim(final View view){

        int[] location = new int[2];
        view.getLocationInWindow(location);
        int centerX = getResources().getDisplayMetrics().widthPixels/2;
        int centerY = getResources().getDisplayMetrics().heightPixels/2;

        View target = getViewByTag(1);
        int[] targetLocation = new int[2];
        target.getLocationInWindow(targetLocation);
        Log.d("wys","[showAnim]  location: x="+location[0]+",y="+location[1]);
        Log.d("wys","[showAnim]  targetLocation: x="+targetLocation[0]+",y="+targetLocation[1]);
        Log.d("wys","[showAnim]  center: x="+centerX+",y="+centerY);

        ObjectAnimator animTransY = ObjectAnimator.ofFloat(view, "translationY", 0, targetLocation[1]+target.getMeasuredHeight()/2 - centerY);
        animTransY.setDuration(300);
        ObjectAnimator animTransX = ObjectAnimator.ofFloat(view, "translationX", 0, targetLocation[0]+target.getMeasuredWidth()/2 - centerX);
        animTransX.setDuration(300);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f);
        animScaleY.setDuration(300);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f);
        animScaleX.setDuration(300);

        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animAlpha.setDuration(300);
        ObjectAnimator animAlpha2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f);
        animAlpha2.setDuration(2000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animScaleY).with(animAlpha2).with(animScaleX).with(animTransY).with(animTransX);
//        animatorSet.play(animAlpha2).after(animAlpha);
        animatorSet.play(animAlpha).before(animScaleY);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getRootView().removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    private int index;
    private View getViewByTag(int tag){
        index++;
        String str="";
        if (index%2==0){
            str = "tag2";
        }else{
            str = "tag0";
        }
        for (int i=0;i<ll_trophy.getChildCount();i++){
            View view = ll_trophy.getChildAt(i);
            if (str.equals(view.getTag())){
                return view;
            }
        }
        return null;
    }

    protected FrameLayout getRootView() {
        return findViewById(R.id.fl_content);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_put_message:
                putMessage();
                break;
            case R.id.btn_put_messages:
                putMessages();
//                showTrophy();
//                playSound();
                break;
            case R.id.btn_update_messages:
                updateMessages();
                break;
        }
    }
    private int position=0;
    private void putMessage(){
        if (position>messages.size() - 1){
            position = 0;
        }
        GslRecMessage message = messages.get(position);
        msgListView.putMessage(message);
        position++;

    }
    private void putMessages(){
        msgListView.putMessages(messages);
    }

    private void updateMessages(){
        msgListView.updateMessages(messages);
    }

    private void initMessages(){
        messages = new ArrayList<>();

        GslRecMessage message = new GslRecMessage();
        message.setContent("第1条聊天消息[困][举手][发呆]");
        message.setNickName("学生a");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("001");
        messages.add(message);

        message = new GslRecMessage();
        message.setContent("第2条聊天消息");
        message.setNickName("学生b");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("002");
        messages.add(message);

        message = new GslRecMessage();
        message.setContent("第3条聊天消息");
        message.setNickName("老师");
        message.setSelf(false);
        message.setUserRole("teacher");
        message.setUserId("003");
        messages.add(message);

        message = new GslRecMessage();
        message.setContent("第4条聊天消息");
        message.setNickName("学生d");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("004");
        messages.add(message);

        message = new GslRecMessage();
        message.setContent("第5条聊天消息");
        message.setNickName("学生e");
        message.setSelf(true);
        message.setUserRole("");
        message.setUserId("005");
        messages.add(message);

        message = new GslRecMessage();
        message.setContent("第6条聊天消息");
        message.setNickName("学生f");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("006");
        messages.add(message);
    }
    private SoundPool soundPool;
    private void initSoundPool(){
        if (Build.VERSION.SDK_INT>=22){
            SoundPool.Builder builder = new SoundPool.Builder();
            //最多播放音频数量
            builder.setMaxStreams(1);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        }else{
            /**
             * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
             * 第二个参数：int streamType：AudioManager中描述的音频流类型
             *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
             */
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        }
    }

    private void playSound(){
        try {
            AssetFileDescriptor afd = getAssets().openFd("music/BackgroundMusic/game_end.mp3");
            final int voiceId = soundPool.load(afd,1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (status==0){
                        //第一个参数soundID
                        //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                        //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                        //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                        //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                        //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
                        soundPool.play(voiceId, 1, 1, 1, 0, 1);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
