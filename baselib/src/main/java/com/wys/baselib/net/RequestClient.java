package com.wys.baselib.net;


import android.text.TextUtils;
import android.util.Log;

import com.wys.baselib.net.common.ICommonParams;
import com.wys.baselib.net.interceptor.HeaderInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class RequestClient {
    public static final long DEFAULT_MILLISECONDS = 60000L;
    public static long REFRESH_TIME = 300L;
    private OkHttpClient okHttpClient;
    private int mRetryCount;

    private static RequestClient instance;

    public static RequestClient getInstance() {
        if (instance == null) {
            synchronized (RequestClient.class) {
                if (instance == null) {
                    instance = new RequestClient();
                }
            }
        }
        return instance;
    }

    private RequestClient(){
        this.mRetryCount = 3;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(60000L, TimeUnit.MILLISECONDS);
        builder.writeTimeout(60000L, TimeUnit.MILLISECONDS);
        builder.connectTimeout(60000L, TimeUnit.MILLISECONDS);
        HttpUtils.SSLParams sslParams = HttpUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpUtils.UnSafeHostnameVerifier);
        builder.addInterceptor(new HeaderInterceptor());
        this.okHttpClient = builder.build();
    }

    /**
     * get请求
     * @param url
     * @param param
     * @param tag
     * @param callback
     */
    public void get(String url,String tag,RequestParam param, Callback callback) {

        String paramStr = param.toString();
        if (!TextUtils.isEmpty(paramStr)){
            url += "?"+paramStr;
        }
        Log.d("RequestClient","[get] url:"+url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .tag(tag)
                .build();


        okHttpClient.newCall(request).enqueue(callback);

    }

    /**
     * json格式参数post
     * @param url
     * @param tag
     * @param param
     * @param callback
     */
    public void postJson(String url, String tag, RequestParam param, Callback callback) {
        RequestBody requestBody = RequestBody.create(HttpUtils.MEDIA_TYPE_JSON, param.toJsonStr());

        final Request request = new Request.Builder()
                .url(url)//请求的url
                .tag(tag)
                .post(requestBody)
                .build();


        Call call = okHttpClient.newCall(request);

        call.enqueue(callback);

    }

    /**
     * Form表单提交，兼容了文件和普通参数同时提交
     * @param url
     * @param tag
     * @param param
     * @param callback
     */
    public void post(String url, String tag, RequestParam param, Callback callback) {
        RequestBody requestBody= null;
        if (param.getFileParams().size()>0){
            requestBody = HttpUtils.createMultipartBody(param.getParams(),param.getFileParams());
        }else {
            requestBody = HttpUtils.createFormBody(param.getParams());
        }

        final Request request = new Request.Builder()
                .url(url)//请求的url
                .tag(tag)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

    }
}
