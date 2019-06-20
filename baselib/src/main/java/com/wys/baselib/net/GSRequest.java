package com.wys.baselib.net;

import com.wys.baselib.net.callback.ResponseCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class GSRequest {

    public static void postRequest(String url, String tag, RequestParam param, ResponseCallback callback) {

        RequestClient.getInstance().post(url, tag, param, initCallback(callback));

    }

    public static void getRequest(String url, String tag, RequestParam param, ResponseCallback callback) {

        RequestClient.getInstance().get(url, tag, param,initCallback(callback));

    }

    private static Callback initCallback(final ResponseCallback callback){
       return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(-1,"网络链接错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {
                    callback.onFailure(-2, "");
                    return;
                }

                int code = response.code();
                switch (code) {
                    case 200:
                        callback.onSuccess(response.body().string());
                        break;
                    default:
                        callback.onFailure(code, response.message());
                }

            }
        };
    }
}
