package com.wys.commonbaselib;

import android.app.Application;

import com.wys.baselib.BaseLib;
import com.wys.baselib.utils.ScreenUtil;
import com.wys.commonbaselib.net.CommonParams;

/**
 * Created by yas on 2019/6/10
 * Describe:
 */
public class MApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.init(this,ScreenUtil.VERTICAL);
        BaseLib.initRequst(new CommonParams());
    }
}
