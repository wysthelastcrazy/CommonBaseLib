package com.wys.commonbaselib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.wys.commonbaselib.bean.VideoBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yas on 2019/11/6
 * Describe:
 */
public class FileUtils {

    public static ArrayList<VideoBean> getVideos(Context mContext,long maxDuration){
        ArrayList<VideoBean> videoBeans = new ArrayList<>();
        ContentResolver mContentResolver = mContext.getContentResolver();
        Cursor c = null;
        c = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        while (c.moveToNext()){
            String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            if (!new File(path).exists()) {
                continue;
            }
            int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));// 视频的id
//            String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 视频名称
//            String resolution = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)); //分辨率
//            long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));// 大小
            long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 时长
//            long date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));//修改时间
            Bitmap thumbnail= getVideoThumbnail(id,mContentResolver);
            if (maxDuration <=0||duration<=maxDuration) {
                VideoBean bean = new VideoBean();
                bean.path = path;
                bean.duration = duration;
                bean.thumbnail = thumbnail;
                videoBeans.add(bean);
            }
        }

        return videoBeans;
    }

    public static ArrayList<VideoBean> getVideos(Context mContext){
        return getVideos(mContext,0);
    }

    // 获取视频缩略图
    public static Bitmap getVideoThumbnail(int id,ContentResolver mContentResolver) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, options);
        return bitmap;
    }
}
