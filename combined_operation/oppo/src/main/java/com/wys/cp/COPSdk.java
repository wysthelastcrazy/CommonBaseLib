package com.wys.cp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.callback.SinglePayCallback;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.game.sdk.common.model.biz.ReqUserInfoParam;
import com.nearme.game.sdk.common.util.AppUtil;
import com.nearme.platform.opensdk.pay.PayResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by yas on 2019/6/28
 * Describe:
 */
public class COPSdk {
    private static final String TAG = "COPSdk";
    public static void init(Context context){
        Log.d(TAG,"[oppo&COPSdk]++++++++++");
        GameCenterSDK.init(Config.appSecret, context);
    }

    /**
     * 登录，非必接
     * @param context
     */
    public static void login(Context context){
        GameCenterSDK.getInstance().doLogin(context, new ApiCallback() {
            @Override
            public void onSuccess(String s) {
                //登录成功
            }

            @Override
            public void onFailure(String s, int i) {
                //登录失败
            }
        });
    }
    private static void getTokenAndSsoid(Context context){
        GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {
            @Override
            public void onSuccess(String resultMsg) {
                try {
                    JSONObject json = new JSONObject(resultMsg);
                    String token = json.getString("token"); String ssoid = json.getString("ssoid");
                } catch (JSONException e) { e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String s, int i) {

            }
        });
    }
    private void getUserInfo(String token,String ssoid){
        GameCenterSDK.getInstance().doGetUserInfo(new ReqUserInfoParam(token,ssoid), new ApiCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG,"[onSuccess]  s:"+s);
                //ssoid为标记用户所需字段，是唯一保持不变的，其余字段为附加字段，
                // 可能会增减，请 不要用附加字段作为进入游戏的限制条件。

            }

            @Override
            public void onFailure(String s, int i) {

            }
        });
    }

    /**
     * 支付,必接
     * @param context
     * @param orderId 订单号，务必保证唯一（不超过100位）
     * @param productName 自定义回调字段，非必填。该字段为交易辅助说明字段，建议填写此次交易相关信息
     * @param amount 金额，单位：分
     */
    public static void pay(Context context,String orderId,String productName,String productDesc,int amount){
        PayInfo payInfo = createPayInfo(orderId,productName,productDesc,amount);
        GameCenterSDK.getInstance().doSinglePay(context, payInfo, new SinglePayCallback() {
            @Override
            public void onCallCarrierPay(PayInfo payInfo, boolean b) {
                //add 运营商支付逻辑，可以忽略该回调方法
            }

            @Override
            public void onSuccess(String resultMsg) {
                //支付成功
            }

            @Override
            public void onFailure(String resultMsg, int resultCode) {
                //支付失败
                if (PayResponse.CODE_CANCEL!=resultCode){

                }else{
                    //取消支付
                }
            }
        });
    }

    /**
     * 组织支付信息
     * @param amount 金额：单位分
     * @return
     */
    private static PayInfo createPayInfo(String orderId,String productName,String productDesc,int amount){
        Random random = new Random();
//        PayInfo payInfo = new PayInfo(System.currentTimeMillis()
////                +random.nextInt(1000)+"","自定义字段",amount);
        PayInfo payInfo = new PayInfo(orderId,"",amount);
        payInfo.setProductName(productName);
        payInfo.setProductDesc(productDesc);
        // payInfo.setUseCachedChannel(true);//设置是否使用上次使用过的支付方式

        payInfo.setCallbackUrl(Config.pay_callback_url);
        return payInfo;
    }

    /**
     * 推出应用，必接
     * 该接口是退出接口，并不是退出账号接口，只有在退出应用的时候需要调用，
     * 应用在切换账号以及注销账号的场景则不能调用，这并不是注销接口。
     * 退出接口的退出逻辑应用可以自己实现，
     * 也可 以使用 sdk 提供的工具类杀掉进程，要确保能彻底退出应用，
     * 没有残留的进程在后台，否则可能在下 次进入应用之后影响支付等功能。
     * @param activity
     */
    public static void exit(final Activity activity){
        GameCenterSDK.getInstance().onExit(activity, new GameExitCallback() {
            @Override
            public void exitGame() {
                AppUtil.exitGameProcess(activity);
            }
        });
    }
}
