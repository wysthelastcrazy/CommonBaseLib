package com.wys.baselib.net;

import android.text.TextUtils;
import android.util.Log;

import com.wys.baselib.net.callback.GSResponse;
import com.wys.baselib.net.callback.IDownloadCallback;
import com.wys.baselib.net.callback.IResponseCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class GSRequest {

    public static void postFormRequest(String url, String tag, RequestParam param, IResponseCallback callback) {

        RequestClient.getInstance().postForm(url, tag, param, initCallback(callback));

    }
    public static void postJsonRequest(String url, String tag, RequestParam param, IResponseCallback callback) {

        RequestClient.getInstance().postJson(url, tag, param, initCallback(callback));

    }

    public static void getRequest(String url, String tag, RequestParam param, IResponseCallback callback) {

        RequestClient.getInstance().get(url, tag, param,initCallback(callback));

    }

    public static void download(String url, final File file, final IDownloadCallback downloadCallback){
        RequestClient.getInstance().downloadFile(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadCallback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = null;
                byte[] buffer = new byte[1024*10];
                int len = 0;
                FileOutputStream fos = null;

                try {
                    inputStream = response.body().byteStream();
                    long total = response.body().contentLength();
                    downloadCallback.onStart();

                    fos = new FileOutputStream(file);
                    long progress = 0;
                    while (true){
                        len = inputStream.read(buffer);
                        if (len!=-1){
                            fos.write(buffer,0,len);
                            progress += len;
//                            long finalProgress = progress;
                            downloadCallback.onProgress(progress,total);
                        }else{
                            break;
                        }
                    }
                    downloadCallback.onComplete();
                }catch (Exception e){
                    downloadCallback.onError(e.getMessage());
                }finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private static Callback initCallback(final IResponseCallback callback){
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
                        try {
                            String rsp = response.body().string();
                            if (TextUtils.isEmpty(rsp)){
                                callback.onFailure(code, "response&body is null");
                            }else {
                                JSONObject jsonObject = new JSONObject(rsp);
                                GSResponse gsResponse = new GSResponse();
                                gsResponse.body = jsonObject;
                                gsResponse.code = code;
                                gsResponse.headers = response.headers();
                                String s = response.header("token");
                                Log.d("wys","[initCallback] s:"+s);
                                callback.onResponse(gsResponse);
                            }
                        }catch (Exception e) {
//                            e.printStackTrace();
                            callback.onFailure(code, e.getMessage());
                        }

                        break;
                    default:
                        callback.onFailure(code, response.message());
                }

            }
        };
    }
}
