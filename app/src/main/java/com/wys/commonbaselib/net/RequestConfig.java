package com.wys.commonbaselib.net;



import com.wys.baselib.net.IRequestConfig;
import com.wys.baselib.net.https.SSLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class RequestConfig implements IRequestConfig {
    @Override
    public Map<String, Object> getParams() {
        Map<String,Object> commonParams = new HashMap<>();
        commonParams.put("key1","value1");
        commonParams.put("key2","value2");
        commonParams.put("key3","value3");
        return commonParams;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("token","user110");
        headers.put("version","1.0.5");
        headers.put("os","android");
        return headers;
    }

    @Override
    public SSLConfig getSSLConfig() {

        return new SSLConfig().setCertificatesIn(null);
    }
}
