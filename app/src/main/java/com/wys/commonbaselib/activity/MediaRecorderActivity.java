package com.wys.commonbaselib.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aixuexi.gushi.R;
import com.gaosiedu.mediarecorder.audio.AudioRecord;
import com.gaosiedu.mediarecorder.camera.CCamera;
import com.gaosiedu.mediarecorder.camera.CameraPreviewView;
import com.gaosiedu.mediarecorder.encoder.MediaEncode;
import com.gaosiedu.mediarecorder.listener.OnNativeCallbackPCMDataListener;
import com.gaosiedu.mediarecorder.shader.PROGRAM;
import com.gaosiedu.mediarecorder.util.CameraUtil;
import com.gaosiedu.mediarecorder.util.DisplayUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author wangyasheng
 * @date 2020-06-02
 * @Describe:
 */
public class MediaRecorderActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MediaRecorderActivity";
    CameraPreviewView cameraPreviewView;

    Button btnStart, btnStop,btnTakePhoto;

    AudioRecord audioRecord;

    private MediaEncode mediaEncode;

    int id = 0;

    private ImageView ivFocus;


    Camera.Size size;

    int height;
    int width;
    private Bitmap bitmapSticker;
    private Bitmap bitmapWatermark;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);
        int screenHeight = DisplayUtil.getScreenHeight(this);
        int screenWidth = DisplayUtil.getScreenWidth(this);
        size = CameraUtil.getCameraSize(this);

        float scale = Math.min(screenHeight * 1.0f / size.height,screenWidth*1.0f/size.width);

        height = (int) (scale * size.height);
        width = (int) (scale * size.width);
        Log.d(TAG,"[onCreate]+++++++++++++width:"+width+",height:"+height);
        bitmapSticker = BitmapFactory.decodeResource(getResources(),R.drawable.img_capture_sticker_donkey);
        bitmapWatermark = BitmapFactory.decodeResource(getResources(),R.drawable.icon_watermark);

        RelativeLayout rlContent = findViewById(R.id.fl_content);

        cameraPreviewView = new CameraPreviewView(this,width,height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        cameraPreviewView.setLayoutParams(params);
        cameraPreviewView.setOnTakePhotoListener(bitmap ->{
            saveBitmap(bitmap);
        });
        cameraPreviewView.setFocusListener(new CCamera.IFocusListener() {
            @Override
            public void onFocus(Rect rect) {

            }
        });
        rlContent.addView(cameraPreviewView);


        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnTakePhoto = findViewById(R.id.takePhoto);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);

//        cameraPreviewView.switchCamera(1);
//        cameraPreviewView.previewAngle(this);
        cameraPreviewView.setSticker(bitmapSticker);
        cameraPreviewView.setWatermark(bitmapWatermark);
        cameraPreviewView.setFragmentShader(PROGRAM.CHARMING);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_start:
                start();
                break;
            case R.id.btn_stop:
                stop();
                break;
            case R.id.takePhoto:
                takePhoto();
                break;

        }
    }

    /**
     * 开始录制视频
     */
    private void start(){
        String recordVideoPath = Environment.getExternalStorageDirectory().getPath()+"/aaa/testabc.mp4";
        File file = new File(recordVideoPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        audioRecord = new AudioRecord();
        audioRecord.startRecord();
        mediaEncode = new MediaEncode(this,cameraPreviewView.getTextureId(),width,height);
        mediaEncode.initEncoder(cameraPreviewView.getEglContext(),
                recordVideoPath,
                MediaFormat.MIMETYPE_VIDEO_AVC,960,540,44100,2);
        mediaEncode.startRecord();

        audioRecord.setOnNativeCallbackPCMDataListener((buffer, size)
                -> mediaEncode.setPCMData(buffer,size));

        mediaEncode.setSticker(bitmapSticker);
        mediaEncode.setWatermark(bitmapWatermark);
    }

    /**
     * 停止录制
     */
    private void stop(){
//        if (cameraPreviewView!=null){
//            cameraPreviewView.release();
//        }
        if (audioRecord!=null){
            audioRecord.stopRecord();
            audioRecord.release();
            audioRecord = null;
        }
        if (mediaEncode!=null){
            mediaEncode.stopRecord();
            mediaEncode = null;
        }
    }

    /**
     * 拍照
     */
    private void takePhoto(){
        Log.d(TAG,"[takePhoto] ++++++++++++++");
        cameraPreviewView.takePhoto();
        cameraPreviewView.pausePreview();
        cameraPreviewView.playSoundEffect(this);
    }

    /**
     * 拍照之后保存图片
     * @param b
     */
    public void saveBitmap(Bitmap b){
        Log.d(TAG,"[saveBitmap] ++++++++++++++");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/aaa/";
        String jpegName = path+"haha"+System.currentTimeMillis()+".jpg";
        File file = new File(jpegName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
