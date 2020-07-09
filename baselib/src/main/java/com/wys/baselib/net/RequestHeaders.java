package com.wys.baselib.net;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2020/7/9
 * @Describe:
 */
public class RequestHeaders {
    private Map<String,Object> mMap;
    public RequestHeaders(){
        mMap = new HashMap<>();
    }

    public RequestHeaders addHeader(String key,Object value){
        mMap.put(key,value);
        return this;
    }


    public Object getHeader(String key) {
        return mMap.get(key);
    }

    public Map<String,Object> getHeaders(){
        return mMap;
    }

}
