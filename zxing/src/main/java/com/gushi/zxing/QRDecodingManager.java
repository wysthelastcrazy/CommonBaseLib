package com.gushi.zxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.Result;
import com.gushi.zxing.camera.CameraManager;
import com.gushi.zxing.decoding.CaptureActivityHandler;
import com.gushi.zxing.view.ViewfinderView;

import java.io.IOException;

/**
 * Created by yas on 2020-03-25
 * Describe:
 */
public class QRDecodingManager implements SurfaceHolder.Callback,IZxingDecodingHolder {
    private final String TAG = "QRManager";
    private Context mContext;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private CaptureActivityHandler handler;

    private boolean hasSurface;
    private CameraManager cameraManager;
    private int framingRectSize;
    private IQRDecodingCallback callback;

    public QRDecodingManager(Context context, SurfaceView surfaceView,
                             ViewfinderView viewfinderView,int framingRectSize){
        this.mContext = context;
        this.framingRectSize = framingRectSize;
        this.surfaceView = surfaceView;
        this.viewfinderView = viewfinderView;
        init();
    }
    private void init(){
        hasSurface = false;
        cameraManager = new CameraManager(mContext,framingRectSize);
        viewfinderView.setCameraManager(cameraManager);
    }
    public void onResume(){
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        restartPreviewAfterDelay(0);
    }
    public void onPause(){
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
    }
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(this, null, null);
            }
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }


    public void setDecodingCallback(IQRDecodingCallback callback){
        this.callback = callback;
    }
    @Override
    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode) {
        String result=rawResult.getText();
        Log.d(TAG,"[handleDecode] result:"+result);
        if (callback!=null){
            callback.handleDecode(result);
        }
    }

    @Override
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }
    public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
		}
    }

    public interface IQRDecodingCallback{
        void handleDecode(String result);
    }
}
