package com.gushi.zxing;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.gushi.zxing.camera.CameraManager;
import com.gushi.zxing.view.ViewfinderView;

/**
 * Created by yas on 2020-03-25
 * Describe:
 */
public interface IZxingDecodingHolder {
    CameraManager getCameraManager();
    void handleDecode(Result obj, Bitmap barcode);
    void drawViewfinder();
    ViewfinderView getViewfinderView();
    Handler getHandler();
}
