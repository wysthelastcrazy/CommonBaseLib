package com.wys.sharelib;

import android.util.Log;

/**
 * @author wangyasheng
 * @date 2020/9/27
 * @Describe:
 */
class GsShareLog {
    //设为false关闭日志
    public static boolean isDebug = false;

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag,msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag,msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag,msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag,msg);
        }
    }

}
