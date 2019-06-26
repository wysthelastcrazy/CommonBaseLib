package com.wys.audio_video_editor.camera;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.wys.audio_video_editor.utils.CameraUtil;

import java.io.IOException;

/**
 * Created by yas on 2019/6/26
 * Describe:相机管理类 主要是Camera的一些设置
 * 包括预览和录制/拍照尺寸、闪光灯、曝光、聚焦、切换摄像头等
 */
public class CameraController implements ICamera{
    /**配置信息*/
    private Config mConfig;
    /**相机*/
    private Camera mCamera;
    private Camera.Size preSize;
    private Camera.Size picSize;

    private Point mPreSize;
    private Point mPicSize;

    public CameraController(){
        mConfig = new Config();
        mConfig.minPreviewWidth = 720;
        mConfig.minPictureWidth = 720;
        mConfig.rate = 1.778f;
    }

    @Override
    public void open(int cameraId) {
        mCamera = Camera.open(cameraId);
        if (mCamera !=null){
            Camera.Parameters param = mCamera.getParameters();
            preSize = CameraUtil.getBestSupportSize(param.getSupportedPreviewSizes(),
                    mConfig.rate,mConfig.minPreviewWidth);
            picSize = CameraUtil.getBestSupportSize(param.getSupportedPictureSizes(),
                    mConfig.rate,mConfig.minPictureWidth);

            param.setPreviewSize(preSize.width,preSize.height);
            param.setPictureSize(picSize.width,picSize.height);

            mCamera.setParameters(param);
            Camera.Size pre = param.getPreviewSize();
            Camera.Size pic = param.getPictureSize();
            mPreSize = new Point(pre.height,pre.width);
            mPicSize = new Point(pic.height,pic.width);
        }
    }

    @Override
    public void setPreviewTexture(SurfaceTexture texture) {
        if (mCamera!=null){
            try {
                mCamera.setPreviewTexture(texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void preview() {
        if (mCamera!=null){
            mCamera.startPreview();
        }
    }

    @Override
    public Point getPreviewSize() {
        return mPreSize;
    }

    @Override
    public Point getPictureSize() {
        return mPicSize;
    }

    @Override
    public void setConfig(Config config) {
        this.mConfig = config;
    }

    @Override
    public boolean close() {
        if (mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        return false;
    }
}
