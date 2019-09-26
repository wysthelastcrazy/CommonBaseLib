package com.wys.cp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.CheckUpdateHandler;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.hwid.handler.SignInHandler;
import com.huawei.android.hms.agent.pay.PaySignUtil;
import com.huawei.android.hms.agent.pay.handler.PayHandler;
import com.huawei.hms.support.api.entity.pay.PayReq;
import com.huawei.hms.support.api.entity.pay.PayStatusCodes;
import com.huawei.hms.support.api.hwid.SignInHuaweiId;
import com.huawei.hms.support.api.pay.PayResultInfo;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by yas on 2019/6/28
 * Describe:
 */
public class COPSdk {
    private static final String TAG = "COPSdk";
    public static void init(Application application){
        Log.d(TAG,"[huawei&COPSdk]++++++++++");
        HMSAgent.init(application);
    }
    public static void connect(final Activity activity){
        HMSAgent.connect(activity, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                checkUpdate(activity);
            }
        });
    }
    private static void checkUpdate(Activity activity){
        HMSAgent.checkUpdate(activity, new CheckUpdateHandler() {
            @Override
            public void onResult(int rst) {

            }
        });
    }
    public static void login(Context context){
        HMSAgent.Hwid.signIn(false, new SignInHandler() {
            @Override
            public void onResult(int rst, SignInHuaweiId result) {
                if (rst == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS&&result!=null){
                    //可以获取帐号的 openid，昵称，头像 at信息
//                    showLog("登录成功=========");
//                    showLog("昵称:" + result.getDisplayName());
//                    showLog("openid:" + result.getOpenId());
//                    showLog("accessToken:" + result.getAccessToken());
//                    showLog("头像url:" + result.getPhotoUrl());
                }else{
                    //登录失败
//                    showLog("登录---error: " + rtnCode);
                }
            }
        });
    }
    public static void pay(Context context,String orderId,String productName,String productDesc,int amount){
        PayReq payReq = createPayReq(orderId,productName,productDesc,amount);
        HMSAgent.Pay.pay(payReq,new PayHandler(){
            @Override
            public void onResult(int rst, PayResultInfo result) {
                if (rst == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS&&result!=null){
                    boolean checkRst = PaySignUtil.checkSign(result,Config.pay_pub_key);
                    Log.d(TAG,"game pay: onResult: pay success and checksign=" + checkRst);
                    if (checkRst){
                        //支付成功并且验签成功，发放商品
                    }else{
                        // 签名失败，需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。
                    }
                }else if (rst == HMSAgent.AgentResultCode.ON_ACTIVITY_RESULT_ERROR
                        || rst == PayStatusCodes.PAY_STATE_TIME_OUT
                        || rst == PayStatusCodes.PAY_STATE_NET_ERROR){
                    // 需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。
                }else{
                    Log.d(TAG,"game pay: onResult: pay fail=" + rst);
                    // 其他错误码意义参照支付api参考
                }
            }
        });

    }

    private static PayReq createPayReq( String orderId,String productName,String productDesc,int totalAmount){
        PayReq payReq = new PayReq();

        /**
         * 生成requestId(String(0,32))
         */
        DateFormat format = new java.text.SimpleDateFormat("yyyyMMddhhmmssSSS");
        int random= new SecureRandom().nextInt() % 100000;
        random = random < 0 ? -random : random;
        String requestId = format.format(new Date());
        requestId = String.format("%s%05d", requestId, random);
        Log.d(TAG,"[createPayReq] requestId:"+requestId);

        /**
         * 生成总金额
         */
        String amount = String.format("%.2f", totalAmount/1000l);

        //商品名称
        payReq.productName = productName;
        //商品描述
        payReq.productDesc = productDesc;
        // 商户ID，来源于开发者联盟的“支付ID”
        payReq.merchantId = Config.cpId;
        // 应用ID，来源于开发者联盟
        payReq.applicationID = Config.appId;
        // 支付金额
        payReq.amount = amount;
        // 商户订单号：开发者在支付前生成，用来唯一标识一次支付请求
        payReq.requestId = orderId;
        // 国家码
        payReq.country = "CN";
        //币种
        payReq.currency = "CNY";
        // 渠道号，0 代表自有应用无渠道，1 代表应用市场渠道， 2 代表预装渠道，
        // 3 代表游戏中心；  游戏设置为3，应用设置为1
        payReq.sdkChannel = 1;
        // 回调接口版本号
        payReq.urlVer = "2";

        // 商户名称，必填，不参与签名。开发者注册的公司名称
        payReq.merchantName = Config.merchantName;
        //分类，必填，不参与签名。该字段会影响风控策略
        // X4：主题,X5：应用商店,  X6：游戏,X7：天际通,X8：云空间,X9：电子书,X10：华为学习,X11：音乐,X12 视频,
        // X31 话费充值,X32 机票/酒店,X33 电影票,X34 团购,X35 手机预购,X36 公共缴费,X39 流量充值
        payReq.serviceCatalog = "X5"; // 应用设置为"X5"，游戏设置为"X6"
        //商户保留信息，选填不参与签名，支付成功后会华为支付平台会原样 回调CP服务端
//        payReq.extReserved = "这是测试支付的功能";

        //对单机应用可以直接调用此方法对请求信息签名，非单机应用一定要在服务器端储存签名私钥，并在服务器端进行签名操作。| For stand-alone applications, this method can be called directly to the request information signature, not stand-alone application must store the signature private key on the server side, and sign operation on the server side.
        // 在服务端进行签名的cp可以将getStringForSign返回的待签名字符串传给服务端进行签名 | The CP, signed on the server side, can pass the pending signature string returned by Getstringforsign to the service side for signature
        payReq.sign = PaySignUtil.rsaSign(PaySignUtil.getStringForSign(payReq), Config.pay_priv_key);

        return payReq;
    }

    public static void exit(){}
}
