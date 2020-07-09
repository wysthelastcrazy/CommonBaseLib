package com.wys.baselib.net.interceptor;


import android.util.Log;

import com.wys.baselib.net.RequestClient;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (RequestClient.getHeaders()!=null){
            for (Map.Entry<String,String> entry: RequestClient.getHeaders().entrySet()){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
