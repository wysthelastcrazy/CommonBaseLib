package com.wys.sharelib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import com.wys.sharelib.callback.IBitmapDownloadCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @author wangyasheng
 * @date 2020/9/25
 * @Describe:
 */
class ShareUtils {
    /**
     * 下载bitmap
     * @param url
     * @param callback
     */
    public static void downloadBitmap(String url, IBitmapDownloadCallback callback){
        new DownloadBitmapTask()
                .setBitmapDownloadCallback(callback)
                .execute(url);
    }
    private static class DownloadBitmapTask extends AsyncTask<String,Void,Bitmap>{
        private IBitmapDownloadCallback callback;
        @Override
        protected Bitmap doInBackground(String... strings) {
            String imgUrl = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(imgUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (callback!=null){
                callback.onSuccess(bitmap);
            }
        }

        protected DownloadBitmapTask setBitmapDownloadCallback(IBitmapDownloadCallback callback){
            this.callback = callback;
            return this;
        }
    }
    /**
     * bitmap转换为byte数组
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断qq是否可用
     */
    public static boolean isQQClientAvailable(Context context) {
        return isAppInstall(context,"com.tencent.mobileqq");
    }

    /**
     * 判断微信是否可用
     */
    public static boolean isWeixinAvilible(Context context) {
        return isAppInstall(context,"com.tencent.mm");
    }

    private static boolean isAppInstall(Context context, String pkgName){
        final PackageManager packageManager = context.getPackageManager();
        // 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        // 获取所有已安装程序的包信息
        if (pinfo != null) {

            for (int i = 0; i < pinfo.size(); i++) {

                String pn = pinfo.get(i).packageName;

                if (pn.equals(pkgName)) {

                    return true;

                }

            }

        }
        return false;
    }
}
