package com.wys.baselib.net.callback;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public interface ResponseCallback {
    void onSuccess(String jsonString);
    void onFailure(int code,String msg);
}
