package com.wys.cp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Created by yas on 2019/6/28
 * Describe:
 */
public class COPSdk {
    private static final String TAG = "COPSdk";
    public static void init(Context context){
        Log.d(TAG,"[official&COPSdk]++++++++++");
    }
    /**
     * 登录，非必接
     * @param context
     */
    public static void login(Context context){

    }
    /**
     * 支付,必接
     * @param context
     * @param orderId 订单号，务必保证唯一（不超过100位）
     * @param productName 自定义回调字段，非必填。该字段为交易辅助说明字段，建议填写此次交易相关信息
     * @param amount 金额，单位：分
     */
    public static void pay(Context context,String orderId,String productName,String productDesc,int amount){

    }
    public static void exit(final Activity activity){

    }
}
