package com.wys.baselib;

import com.wys.baselib.net.IRequestConfig;
import com.wys.baselib.net.RequestClient;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class BaseLib {
    public static void initRequest(IRequestConfig config){
        RequestClient.setRequestConfig(config);
    }
}
