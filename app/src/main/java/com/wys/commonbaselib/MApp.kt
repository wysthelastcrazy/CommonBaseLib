package com.wys.commonbaselib

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.wys.baselib.BaseLib
import com.wys.baselib.utils.ScreenUtil
import com.wys.commonbaselib.net.RequestConfig

/**
 * Created by yas on 2019/6/10
 * Describe:
 */
class MApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ScreenUtil.init(this, ScreenUtil.VERTICAL)
        BaseLib.initRequest(RequestConfig())
        handler = Handler(Looper.getMainLooper())

    }

    companion object {
        var handler: Handler? = null
            private set
    }
}