package com.wys.baselib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

/**
 * Created by yas on 2019/5/10
 * Describe:
 */
public class ScreenUtil {
    private static final String TAG="ScreenUtil";

    private static WindowManager wm;
    private static DisplayMetrics displayMetrics;

    private static int screenWidth;
    private static int screenHeight;
    private static Context mContext;

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;



    @IntDef({HORIZONTAL,VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation{}

    public static void init(@NonNull Context context,@Orientation int orientation){
        mContext = context;
        if (wm == null) {
            wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }

        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
        }

        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        switch (orientation) {
            case HORIZONTAL:
                screenWidth = Math.round((width + getNavigationBarHeight()) * 1.0f / 10) * 10;
                screenHeight = height;
                break;
            case VERTICAL:
                screenWidth = Math.round((height + getNavigationBarHeight()) * 1.0f / 10) * 10;
                screenHeight = width;
                break;
        }
//        if (width > height) {
//            screenWidth = Math.round((width + getNavigationBarHeight()) * 1.0f / 10) * 10;
//            screenHeight = height;
//        } else {
//            screenWidth = Math.round((height + getNavigationBarHeight()) * 1.0f / 10) * 10;
//            screenHeight = width;
//        }
    }

    //获取虚拟按键的高度
    private static int getNavigationBarHeight() {
        int result = 0;
        if (hasNavBar()) {
            Resources res = mContext.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    public static int getScreenWidth(){
        return screenWidth;
    }
    public static int getScreenHeight() {
        return screenHeight;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static boolean hasNavBar() {
        Resources res = mContext.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(mContext).hasPermanentMenuKey();
        }
    }
    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}
