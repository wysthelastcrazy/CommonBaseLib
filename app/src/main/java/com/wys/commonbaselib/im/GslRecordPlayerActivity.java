package com.wys.commonbaselib.im;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wys.commonbaselib.R;

import java.util.ArrayList;

/**
 * Created by yas on 2020-02-19
 * Describe:
 */
public class GslRecordPlayerActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_base_right;
    private GslRecordMessageListView msgListView;
    private Button btn_put_message;
    private Button btn_put_messages;

    private LinearLayout ll_trophy;

    private ArrayList<GslRecordMessage> messages;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsl_record_player);

        initMessages();
        ll_base_right = findViewById(R.id.ll_base_right);
        msgListView = new GslRecordMessageListView(this);
        ll_base_right.addView(msgListView);
        msgListView.setRoomId("001");

        btn_put_message = findViewById(R.id.btn_put_message);
        btn_put_message.setOnClickListener(this);
        btn_put_messages = findViewById(R.id.btn_put_messages);
        btn_put_messages.setOnClickListener(this);
        initTrophy();
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
//                putMessages();
                showTrophy();
                break;
        }
    }
    private int position=0;
    private void putMessage(){
        if (position>messages.size() - 1){
            position = 0;
        }
        GslRecordMessage message = messages.get(position);
        msgListView.putMessage(message);
        position++;

    }
    private void putMessages(){
        msgListView.putMessages(messages);
    }

    private void initMessages(){
        messages = new ArrayList<>();

        GslRecordMessage message = new GslRecordMessage();
        message.setContent("第1条聊天消息++++++++++++++++++++++++");
        message.setNickName("学生a");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("001");
        messages.add(message);

        message = new GslRecordMessage();
        message.setContent("第2条聊天消息");
        message.setNickName("学生b");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("002");
        messages.add(message);

        message = new GslRecordMessage();
        message.setContent("第3条聊天消息");
        message.setNickName("老师");
        message.setSelf(false);
        message.setUserRole("teacher");
        message.setUserId("003");
        messages.add(message);

        message = new GslRecordMessage();
        message.setContent("第4条聊天消息");
        message.setNickName("学生d");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("004");
        messages.add(message);

        message = new GslRecordMessage();
        message.setContent("第5条聊天消息");
        message.setNickName("学生e");
        message.setSelf(true);
        message.setUserRole("");
        message.setUserId("005");
        messages.add(message);

        message = new GslRecordMessage();
        message.setContent("第6条聊天消息");
        message.setNickName("学生f");
        message.setSelf(false);
        message.setUserRole("");
        message.setUserId("006");
        messages.add(message);
    }
}
