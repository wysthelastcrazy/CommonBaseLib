package com.wys.baselib.net.callback;

import org.json.JSONObject;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public interface ResponseCallback {
    void onSuccess(JSONObject jsonResponse);
    void onFailure(int code,String msg);
}
