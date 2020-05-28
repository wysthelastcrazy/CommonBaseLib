package com.gaosiedu.mediarecorder.util;

import android.content.Context;

public class DisplayUtil {

    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

}
