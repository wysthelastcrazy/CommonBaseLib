package com.wys.commonbaselib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wys.baselib.net.GSRequest;
import com.wys.baselib.net.RequestParam;
import com.wys.baselib.net.callback.ResponseCallback;
import com.wys.baselib.utils.ScreenUtil;

import org.json.JSONObject;

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
                GSRequest.getRequest("https://scoreenglish.aixuexi.com/student/class/record", null,
                        new RequestParam().addParam("fbClassId","123").addParam("studentId","4321"),
                        new ResponseCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonString) {
                        Log.d("MainActivity","[onSuccess] jsonString:"+jsonString);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.d("MainActivity","[onFailure] msg:"+msg+"   code:"+code);
                    }
                });
                break;
        }
    }
}
