package com.wys.sharelib;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2020/10/10
 * @Describe:
 */
class SharePlatformConfig {

    public static Map<GS_SHARE_PLATFORM,SharePlatform> configs = new HashMap<>();
    static{
        configs.put(GS_SHARE_PLATFORM.WEIXIN,new APPIDPlatform(GS_SHARE_PLATFORM.WEIXIN));
    }
    public static void setWeixin(String appId,String appSecret){
        APPIDPlatform wxPlatform = (APPIDPlatform) configs.get(GS_SHARE_PLATFORM.WEIXIN);
        wxPlatform.appId = appId;
        wxPlatform.appKey = appSecret;
    }
    public interface SharePlatform{
        GS_SHARE_PLATFORM getPlatformName();
        boolean isConfigured();
        String getAppId();
        String getAppSecret();
    }

    public static class APPIDPlatform implements SharePlatform{
        public String appId = null;
        public String appKey = null;
        private GS_SHARE_PLATFORM platform;
        public APPIDPlatform(GS_SHARE_PLATFORM platform) {
            this.platform = platform;
        }

        @Override
        public GS_SHARE_PLATFORM getPlatformName() {
            return platform;
        }

        @Override
        public boolean isConfigured() {
            return !TextUtils.isEmpty(appId)&&!TextUtils.isEmpty(appKey);
        }

        @Override
        public String getAppId() {
            return appId;
        }

        @Override
        public String getAppSecret() {
            return appKey;
        }
    }

    /**
     * 分享平台
     */
    public enum GS_SHARE_PLATFORM {
        //微信平台
        WEIXIN,
        //QQ平台
        QQ
    }
}
