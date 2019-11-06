package com.wys.commonbaselib;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.wys.baselib.BaseLib;
import com.wys.baselib.utils.ScreenUtil;
import com.wys.commonbaselib.net.RequestConfig;

/**
 * Created by yas on 2019/6/10
 * Describe:
 */
public class MApp extends Application {
    private static Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.init(this,ScreenUtil.VERTICAL);
        BaseLib.initRequest(new RequestConfig());
        handler = new Handler(Looper.getMainLooper());
    }
    public static Handler getHandler() {
        return handler;
    }
}
