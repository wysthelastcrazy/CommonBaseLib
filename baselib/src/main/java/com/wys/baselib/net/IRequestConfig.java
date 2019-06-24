package com.wys.baselib.net;

import com.wys.baselib.net.https.SSLConfig;

import java.util.Map;

/**
 * Created by yas on 2019/6/24
 * Describe:
 */
public interface IRequestConfig {
    //添加公共参数
    Map<String,Object> getParams();
    //添加公共头
    Map<String,String> getHeaders();
    //https证书设置
    SSLConfig getSSLConfig();
}
