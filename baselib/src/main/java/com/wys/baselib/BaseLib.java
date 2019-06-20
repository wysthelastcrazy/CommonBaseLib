package com.wys.baselib;

import com.wys.baselib.net.RequestConfig;
import com.wys.baselib.net.common.ICommonParams;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class BaseLib {
    public static void initRequst(ICommonParams params){
        RequestConfig.params = params;
    }
}
