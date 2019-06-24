package com.wys.baselib.net.callback;


/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public interface IResponseCallback {
    void onResponse(GSResponse response);
    void onFailure(int code,String msg);
}
