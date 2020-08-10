package com.wys.baselib.utils;

import android.content.Context;

/**
 * @author wangyasheng
 * @date 2020/8/7
 * @Describe:
 */
public class ResourceUtil {
    //在dimen资源中获取尺寸
    public static float getDimen(Context context,int resId){
        return context.getResources().getDimension(resId);
    }
}
