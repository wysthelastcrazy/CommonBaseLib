package com.wys.baselib.net;

import com.wys.baselib.net.common.ICommonParams;

import java.util.Map;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class RequestConfig {
    public static ICommonParams params;

    public static Map<String,Object> getCommonParams(){
        if (params!=null){
            return params.getParams();
        }
        return null;
    }
    public static Map<String,String> getHeaders(){
        if (params!=null){
            return params.getHeaders();
        }
        return null;
    }

}
