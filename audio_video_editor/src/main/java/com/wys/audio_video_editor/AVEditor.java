package com.wys.audio_video_editor;

import android.content.Context;

/**
 * Created by yas on 2019/6/26
 * Describe:
 */
public class AVEditor {
    /**
     * 屏幕宽高
     */
    public static int mScreenWidth;
    public static int mScreenHeight;
    public static Context mContext;

    public static void init(Context context,int screenWidth,int screenHeight ) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mContext = context;
    }
}
