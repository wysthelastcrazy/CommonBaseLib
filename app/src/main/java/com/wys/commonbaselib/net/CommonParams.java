package com.wys.commonbaselib.net;

import com.wys.baselib.net.common.ICommonParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class CommonParams implements ICommonParams {
    @Override
    public Map<String, Object> getParams() {
        Map<String,Object> commonParams = new HashMap<>();
        commonParams.put("key1","value1");
        commonParams.put("key2","value2");
        commonParams.put("key3","value3");
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("token","user110");
        return headers;
    }
}
