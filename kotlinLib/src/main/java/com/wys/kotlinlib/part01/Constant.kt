package com.wys.kotlinlib.part01

interface Constant {
    /**
     * 渠道
     */
    interface Channel {
        companion object {
            const val CHANNEL_OFFICIAL = "official" //官网渠道
            const val CHANNEL_LENOVO = "lenovo" //联想
            const val CHANNEL_OPPO = "oppo" //oppo
            const val CHANNEL_VIVO = "vivo" //vivo
            const val CHANNEL_HUAWEI = "huawei" //华为
            const val CHANNEL_XIAOMI = "xiaomi" //小米
            const val CHANNEL_YYB = "yingyongbao" //应用宝
            const val CHANNEL_BAIDU = "baidu" //百度
            const val CHANNEL_QH360 = "qh360" //360助手
            const val CHANNEL_MEIZU = "meizu" //魅族
            const val CHANNEL_JINLI = "jinli" //金立
            const val CHANNEL_READBOY = "readboy" //读书郎
            const val CHANNEL_BBK = "bubugao" //步步高
            const val CHANNEL_YOUXUEPAI = "youxuepai" //优学派
            const val CHANNEL_XIWO = "xiwo" //希沃
        }
    }

    /**
     * 支付渠道
     */
    interface PayChannel {
        companion object {
            const val ALI_PAY = 1 //支付宝
            const val WX_PAY = 2 //微信
            const val OPPO_PAY = 3 //OPPO支付
            const val HUAWEI_PAY = 4 //华为支付
            const val MI_PAY = 6 //小米支付
        }
    }

    /**大陆分类配置 */
    interface LandType {
        companion object {
            const val LAND_TYPE_BASE = 1 //基础80首必备古诗大陆
            const val LAND_TYPE_POET = 2 //诗人篇大陆type
            const val LAND_TYPE_EXPAND = 3 //拓展篇大陆type
        }
    }

    /**
     * 首页运营活动内链动作action定义
     */
    interface IHomeOperateAction {
        companion object {
            /**
             * 购买
             */
            const val ACTION_BUY = "buy"

            /**
             * 邀请助力
             */
            const val ACTION_INVITE = "invite"

            /**
             * 抽奖
             */
            const val ACTION_LOTTERY = "lottery"
        }
    }

    companion object {
        /**
         * 打开app的次数
         */
        const val SP_APP_VISIT_TIME = "sp_app_visit_time"

        /**是否已经跳转应用商店 */
        const val SP_IS_GOTO_SHOP = "sp_goto_shop" + "_"

        /**是否得过三星 */
        const val SP_IS_GET_ALL_STAR = "sp_isGetAllStar" + "_"

        /**是否完整观看过一个视频 */
        const val SP_IS_FINSH_WATCH_VIDEO = "sp_isFinsh_watch_video" + "_"

        /**是否开启护眼模式 */
        const val SP_IS_OPEN_EYE_SCREEN_MODEL = "is_open_eye_screen_model"

        /**新手解锁提醒 */
        const val SP_UNLOCK_TIPS = "UnlockTips_phone_"

        /**设备id */
        const val SP_DEVICE_ID = "deviceId"
        const val SP_FREE_POETRY_ID = "free_poetry_id"

        /**第三方登录的默认头像 */
        const val SP_DEFAULT_AVATAR = "default_avatar_id_"
        const val SP_NEED_BOUND_TEL = "bound_tel_unionid_"

        /**背景音乐开关 */
        const val SP_SWITCH_BG_MUSIC = "switch_bg_music"

        /**音效开关 */
        const val SP_SWITCH_MUSIC = "switch_music"

        /**小诗仙播放引导 */
        const val SP_LITTLE_POET_GUIDE = "is_show_little_poet_guide"
    }
}