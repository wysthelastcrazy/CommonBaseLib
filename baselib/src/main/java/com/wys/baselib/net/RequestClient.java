package com.wys.baselib.net;


import android.text.TextUtils;
import android.util.Log;

import com.wys.baselib.net.ext.ProgressRequestBody;
import com.wys.baselib.net.ext.ProgressRequestListener;
import com.wys.baselib.net.https.SSLConfig;
import com.wys.baselib.net.interceptor.HeaderInterceptor;

import java.util.Map;
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

    private static IRequestConfig mConfig;

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
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        HttpUtils.SSLParams sslParams = HttpUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpUtils.UnSafeHostnameVerifier);
        builder.addInterceptor(new HeaderInterceptor());
//        builder.addInterceptor(new RetryInterceptor());
        this.okHttpClient = builder.build();
    }

    /**
     * get请求
     * @param url
     * @param param
     * @param tag
     * @param callback
     */
    public void get(String url,String tag,RequestParam param,RequestHeaders headers, Callback callback) {

        String paramStr = param.toString();
        if (!TextUtils.isEmpty(paramStr)){
            url += "?"+paramStr;
        }
        Request request = addHeaders(new Request.Builder(),headers)
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
    public void postJson(String url, String tag, RequestParam param,RequestHeaders headers, Callback callback) {
        RequestBody requestBody = RequestBody.create(HttpUtils.MEDIA_TYPE_JSON, param.toJsonStr());

        final Request request = addHeaders(new Request.Builder(),headers)
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
    public void postForm(String url, String tag, RequestParam param, RequestHeaders headers,Callback callback) {
        RequestBody requestBody= null;
        if (param.getFileParams().size()>0){
            requestBody = HttpUtils.createMultipartBody(param.getParams(),param.getFileParams());
        }else {
            requestBody = HttpUtils.createFormBody(param.getParams());
        }

        final Request request = addHeaders(new Request.Builder(),headers)
                .url(url)//请求的url
                .tag(tag)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

    }


    public void downloadFile(String url,Callback callback){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void uploadFile(String url, String tag, RequestParam param, Callback callback, ProgressRequestListener listener){
        RequestBody requestBody= null;
        if (param.getFileParams().size()>0){
            requestBody = HttpUtils.createMultipartBody(param.getParams(),param.getFileParams());
        }else {
            requestBody = HttpUtils.createFormBody(param.getParams());
        }

        final Request request = new Request.Builder()
                .url(url)//请求的url
                .tag(tag)
                .post(new ProgressRequestBody(requestBody,listener))
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


    /**
     * 添加请求头（非公共header）
     * @param builder
     * @param headers
     * @return
     */
    private Request.Builder addHeaders(Request.Builder builder, RequestHeaders headers){
        if (headers!=null&&headers.getHeaders()!=null){
            for (Map.Entry<String,Object> entry:headers.getHeaders().entrySet()){
                Log.d("wys","[addHeaders] key:"+entry.getKey()+",value:"+entry.getValue());
                builder.addHeader(entry.getKey(),entry.getValue()+"");
            }
        }
        return builder;
    }


    public static void setRequestConfig(IRequestConfig config){
        mConfig = config;
    }

    public static Map<String,Object> getCommonParams(){
        if (mConfig!=null){
            return mConfig.getParams();
        }
        return null;
    }
    public static Map<String,String> getHeaders(){
        if (mConfig!=null){
            return mConfig.getHeaders();
        }
        return null;
    }
    public static SSLConfig getSSLConfig(){
        if (mConfig!=null){
            return mConfig.getSSLConfig();
        }
        return null;
    }
}
