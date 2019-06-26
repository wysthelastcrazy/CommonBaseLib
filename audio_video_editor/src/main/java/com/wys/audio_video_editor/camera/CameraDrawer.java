package com.wys.audio_video_editor.camera;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;


import com.wys.audio_video_editor.utils.OpenGLUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yas on 2019/3/19
 * Describe:管理图像绘制的类
 * 主要用于管理各种滤镜、画面旋转、视频编码录制等
 */
public class CameraDrawer{


    private SurfaceTexture mSurfaceTextrue;
    /**预览数据的宽高*/
    private int mPreviewWidth=0,mPreviewHeight=0;
    /**控件的宽高*/

    private int textureID;

    private DirectDrawer mDirectDrawer;

    public CameraDrawer(){

    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        textureID = OpenGLUtil.createTextureID();
        mSurfaceTextrue = new SurfaceTexture(textureID);
        mDirectDrawer  = new DirectDrawer(textureID);
    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //从图像流中将纹理图像更新为最近的帧
        mSurfaceTextrue.updateTexImage();
        mDirectDrawer.draw();
    }


    public SurfaceTexture getTexture() {
        return mSurfaceTextrue;
    }

    /**设置预览效果的size*/
    public void setPreviewSize(int width,int height){
        if (mPreviewWidth != width || mPreviewHeight != height){
            mPreviewWidth = width;
            mPreviewHeight = height;
        }
    }

}

