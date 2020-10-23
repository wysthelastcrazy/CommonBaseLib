package com.wys.sharelib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wys.sharelib.callback.IBitmapDownloadCallback;
import com.wys.sharelib.callback.IGsShareCallbackListener;

/**
 * @author wangyasheng
 * @date 2020/9/25
 * @Describe:分享管理类
 */
public class GsShareSdkManager {
    private final String TAG = "GsShareSdkManager";
    private static GsShareSdkManager instance;
    private IWXAPI iwxapi;
    private IGsShareCallbackListener listener;
    private Context mContext;
    private static final int THUMB_SIZE = 150;
    private GsShareSdkManager() {
    }

    public static GsShareSdkManager getInstance() {
        if (instance == null) {
            synchronized (GsShareSdkManager.class) {
                if (instance == null) {
                    instance = new GsShareSdkManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化分享相关的sdk
     * @param context
     */
    public void initShareSDK(Context context){
        mContext = context;
        if (SharePlatformConfig.configs.get(SharePlatformConfig.GS_SHARE_PLATFORM.WEIXIN).isConfigured()) {
            initWxSDK(context);
        }
//        GsShareLog.isDebug = BuildConfig.DEBUG;
    }

    /**
     * 向微信终端注册应用id
     * @param context
     */
    private void initWxSDK(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID, true);
        iwxapi.registerApp(Constants.WX_APP_ID);
    }

    /**===============文字类型分享==================**/
    /**
     * 文字类型分享对外统一方法，根据GS_SHARE_TYPE自行调用对应渠道的分享逻辑
     * @param text
     * @param description
     * @param type
     * @param listener
     */
    public void shareText(String text, String description, GS_SHARE_TYPE type, IGsShareCallbackListener listener){
        this.listener = listener;
        switch (type){
            case WEIXIN_SESSION:
                //微信聊天分享
                wxShareText(text,description,SendMessageToWX.Req.WXSceneSession);
                break;
            case WEIXIN_CIRCLE:
                //微信朋友圈分享
                wxShareText(text,description,SendMessageToWX.Req.WXSceneTimeline);
                break;
        }

    }

    /**
     * 微信 文字类型分享
     * @param text
     * @param description
     * @param scene
     */
    private void wxShareText(String text,String description,int scene){
        if (iwxapi == null){
            GsShareLog.e(TAG,"IWXAPI 未初始化");
            if (listener!=null){
                listener.onShareError("微信分享sdk未初始化");
            }
            return;
        }
        //初始化WXTextObject对象，填写分享的文本内容
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = description;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = scene;
        iwxapi.sendReq(req);
    }


    /**===============webPage分享==================**/
    /**
     * web页分享对外统一方法，根据GS_SHARE_TYPE自行调用对应渠道的分享逻辑。
     * @param targetUrl
     * @param title
     * @param description
     * @param thumb
     * @param type
     * @param listener
     */
    public void shareWebPage(String targetUrl, String title, String description, Bitmap thumb,
                             GS_SHARE_TYPE type, IGsShareCallbackListener listener){
        this.listener = listener;
        switch (type){
            case WEIXIN_SESSION:
                //微信聊天分享
                wxShareWebPage(targetUrl,title,description,thumb,SendMessageToWX.Req.WXSceneSession);
                break;
            case WEIXIN_CIRCLE:
                //微信朋友圈分享
                wxShareWebPage(targetUrl,title,description,thumb,SendMessageToWX.Req.WXSceneTimeline);
                break;
        }
    }
    public void shareWebPage(final String targetUrl, final String title, final String description, String thumbUrl,
                             final GS_SHARE_TYPE type, final IGsShareCallbackListener listener){
        ShareUtils.downloadBitmap(thumbUrl, new IBitmapDownloadCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                shareWebPage(targetUrl,title,description,bitmap,type,listener);
            }
        });
    }
    /**
     * 微信 网页类型分享
     */
    private void wxShareWebPage(String targetUrl,String title,String description,Bitmap thumbBmp,int scene) {
       if (iwxapi == null){
           GsShareLog.e(TAG,"IWXAPI 未初始化");
           if (listener!=null){
               listener.onShareError("微信分享sdk未初始化");
           }
           return;
       }
//       if (!ShareUtils.isWeixinAvilible(mContext)){
//
//       }
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = targetUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        if (thumbBmp == null){
            thumbBmp = BitmapFactory.decodeResource(mContext.getResources(),android.R.drawable.sym_def_app_icon);
        }
        if (thumbBmp!=null&&!thumbBmp.isRecycled()) {
            Bitmap scaleThumbBmp = Bitmap.createScaledBitmap(thumbBmp, THUMB_SIZE, THUMB_SIZE, true);
            thumbBmp.recycle();
            msg.thumbData = ShareUtils.bmpToByteArray(scaleThumbBmp, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        iwxapi.sendReq(req);
    }

    /**===============音乐分享==================**/
    /**
     * 音乐分享对外统一方法，根据GS_SHARE_TYPE自行调用对应渠道的音乐分享逻辑
     * @param musicUrl  音频url
     * @param targetUrl 点击分享项的跳转url，域名需在微信后台申请设置白名单，否则分享发送失败
     * @param title
     * @param description
     * @param thumbUrl
     * @param type
     * @param listener
     */
    public void shareMusic(final String musicUrl, final String targetUrl, final String title,
                           final String description, String thumbUrl, final GS_SHARE_TYPE type,
                           final IGsShareCallbackListener listener){
        ShareUtils.downloadBitmap(thumbUrl, new IBitmapDownloadCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                shareMusic(musicUrl,targetUrl,title,description,bitmap,type,listener);
            }
        });
    }
    public void shareMusic(String musicUrl,String targetUrl,String title,String description,Bitmap thumb,
                           GS_SHARE_TYPE type, IGsShareCallbackListener listener){
        this.listener = listener;
        switch (type){
            case WEIXIN_SESSION:
                wxShareMusic(musicUrl,targetUrl,title,description,thumb,SendMessageToWX.Req.WXSceneSession);
                break;
            case WEIXIN_CIRCLE:
                wxShareMusic(musicUrl,targetUrl,title,description,thumb,SendMessageToWX.Req.WXSceneTimeline);
                break;
        }
    }

    /**
     * 微信 音乐分享处理
     * @param musicUrl
     * @param targetUrl
     * @param title
     * @param description
     * @param thumb
     * @param scene
     */
    private void wxShareMusic(String musicUrl,String targetUrl,String title,String description,
                              Bitmap thumb,int scene){
        if (iwxapi == null){
            GsShareLog.e(TAG,"IWXAPI 未初始化");
            if (listener!=null){
                listener.onShareError("微信分享sdk未初始化");
            }
            return;
        }
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = targetUrl;
        musicObject.musicDataUrl = musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = musicObject;
        msg.title = title;
        msg.description = description;

        if (thumb == null){
            thumb = BitmapFactory.decodeResource(mContext.getResources(),android.R.drawable.sym_def_app_icon);
        }
        Bitmap scaleThumbBmp = Bitmap.createScaledBitmap(thumb, THUMB_SIZE, THUMB_SIZE, true);
        thumb.recycle();
        msg.thumbData = ShareUtils.bmpToByteArray(scaleThumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = scene;
        //调用api接口，发送数据到微信
        iwxapi.sendReq(req);
    }
    /**
     * 微信分享回调处理
     * @param resp
     */
    protected void wxShareCallback(BaseResp resp) {
        GsShareLog.d(TAG, "[wxShareCallback] ++++++++");
        if (resp != null&&listener!=null) {
            GsShareLog.d(TAG, "[wxShareCallback] rspCode:" + resp.errCode + "  ,rspMsg:" + resp.errStr
                    + "  ,transaction:" + resp.transaction);
            int code = resp.errCode;
            switch (code) {
                /**
                 * 注意：微信为减少"强制分享至不同群"的情况，在微信客户端6.7.2版本之后
                 * 用户从app中分享消息给微信好友、朋友圈时，开发者将无法获知用户是否分享完成。
                 * 原先的cancel事件和success事件统一为success事件。
                 */
                case BaseResp.ErrCode.ERR_OK:
                    GsShareLog.d(TAG, "[wxShareCallback] 分享成功！");
                    listener.onShareSuccess();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    GsShareLog.d(TAG, "[wxShareCallback] 分享取消！");
                    listener.onShareCancel();
                    break;
                default:
                    GsShareLog.d(TAG, "[wxShareCallback] 分享错误：" + resp.errStr);
                    listener.onShareError(resp.errStr);
                    break;
            }
        } else if (resp == null){
            GsShareLog.d(TAG, "[wxShareCallback] resp is null");
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
