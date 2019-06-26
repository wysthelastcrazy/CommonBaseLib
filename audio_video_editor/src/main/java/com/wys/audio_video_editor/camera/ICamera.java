package com.wys.audio_video_editor.camera;

import android.graphics.Point;
import android.graphics.SurfaceTexture;

/**
 * Created by yas on 2019/6/26
 * Describe:
 */
public interface ICamera {
     void open(int cameraId);

     void setPreviewTexture(SurfaceTexture texture);

     void preview();

     Point getPreviewSize();
     Point getPictureSize();

     void setConfig(Config config);

     boolean close();

     class Config{
         public float rate = 1.778f;
         public int minPreviewWidth;
         public int minPictureWidth;
     }
}
