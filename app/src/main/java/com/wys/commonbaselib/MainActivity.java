package com.wys.commonbaselib;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wys.baselib.net.GSRequest;
import com.wys.baselib.net.RequestParam;
import com.wys.baselib.net.callback.GSResponse;
import com.wys.baselib.net.callback.IDownloadCallback;
import com.wys.baselib.net.callback.IResponseCallback;
import com.wys.baselib.utils.ScreenUtil;
import com.wys.commonbaselib.bean.UserBean;
import com.wys.commonbaselib.net.BusinessCallback;
import com.wys.commonbaselib.utils.Md5Util;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView tv_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_info = findViewById(R.id.tv_info);
        tv_info.setText("width:"+ScreenUtil.getScreenWidth()+" , height:"+ScreenUtil.getScreenHeight());
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_recycler:
                Intent intent = new Intent(this,RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_net:
                GSRequest.postFormRequest("http://c.dev.aixuexi.com/password/login", null,
                        new RequestParam().addParam("username","19990000001")
                                .addParam("password","123456"),
                        new IResponseCallback() {
                    @Override
                    public void onResponse(GSResponse gsResponse) {

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                    }
                });
                break;
            case R.id.btn_postJson:
                GSRequest.postJsonRequest("https://gushiapi.egaosi.com/login/login", null,
                        new RequestParam().addParam("mobile", "18811426939")
                                .addParam("pwd", Md5Util.parseMD5("111111"))
                                .addParam("type",2),
                        new BusinessCallback<UserBean>(UserBean.class) {
                            @Override
                            public void onSuccess(UserBean userBean) {
                                Log.d("MainActivity","[onSuccess] userBean:"+userBean.getUserInfo().getNickName());
                            }

                            @Override
                            public void onError(int code, String errorMsg) {

                            }
                        });
                break;
            case R.id.btn_downFile:
                File file = new File(Environment.getExternalStorageDirectory(), "/test.jpg");
                GSRequest.download("https://timgsa." +
                                "baidu.com/timg?image&quality=" +
                                "80&size=b9999_10000&sec=1561372545884&di=" +
                                "672d238c03f5d3cfdce839cb1ccf396c&imgtype=0&" +
                                "src=http%3A%2F%2Fp4.so.qhmsg.com%2Ft0108dfa8062295f6a9.jpg",
                        file,
                        new IDownloadCallback() {
                            @Override
                            public void onStart() {
                                Log.d("MainActivity111","[onStart] ++++++++");
                            }

                            @Override
                            public void onProgress(long currProgress, long total) {
                                Log.d("MainActivity111","[onProgress] ++++++++total:"+total+" ,+currProgress:"+currProgress);
                            }

                            @Override
                            public void onComplete() {
                                Log.d("MainActivity111","[onComplete] ++++++++");
                            }

                            @Override
                            public void onError(String errorMsg) {
                                Log.d("MainActivity111","[onError] ++++++++ errorMsg:"+errorMsg);
                            }
                        });
                break;
        }
    }
}
