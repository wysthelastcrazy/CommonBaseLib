package com.wys.commonbaselib.net;

import com.google.gson.Gson;
import com.wys.baselib.net.callback.GSResponse;
import com.wys.baselib.net.callback.ResponseCallback;

import org.json.JSONObject;

/**
 * Created by yas on 2019/6/24
 * Describe:
 */
public abstract class BusinessCallback<T> implements ResponseCallback {
    private Class<T> clz;


    public BusinessCallback(Class clz) {
        this.clz = clz;
    }
    @Override
    public void onResponse(GSResponse response) {
        if (response!=null&&response.body!=null){
            JSONObject body = response.body;
            int code = body.optInt("code");
            String msg = body.optString("msg");
            String data = body.optString("data");

            if (code == 200){
                Gson gson = new Gson();
                T t=gson.fromJson(data,clz);
                onSuccess(t);
            }else{
                onError(code,msg);
            }

        }else{
            onError(-1,"response or response&body is null");
        }

    }

    @Override
    public void onFailure(int code, String msg) {
        onError(code,msg);
    }

    public abstract void onSuccess(T t);
    public abstract void onError(int code,String errorMsg);
}
