package com.wys.commonbaselib.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gaosiedu.mediarecorder.audio.AudioRecord;
import com.gaosiedu.mediarecorder.camera.CameraPreviewView;
import com.gaosiedu.mediarecorder.encoder.MediaEncode;
import com.gaosiedu.mediarecorder.listener.OnNativeCallbackPCMDataListener;
import com.gaosiedu.mediarecorder.shader.PROGRAM;
import com.gaosiedu.mediarecorder.util.CameraUtil;
import com.gaosiedu.mediarecorder.util.DisplayUtil;
import com.wys.commonbaselib.R;

import java.io.BufferedOutputStream;
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
    CameraPreviewView cameraPreviewView;

    Button btnStart, btnStop,btnTakePhoto;

    AudioRecord audioRecord;

    private MediaEncode mediaEncode;

    int id = 0;

    private ImageView ivFocus;


    Camera.Size size;

    int height;
    int width;
    private Bitmap bitmap;
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
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_capture_sticker_donkey);


        RelativeLayout rlContent = findViewById(R.id.fl_content);

        cameraPreviewView = new CameraPreviewView(this,width,height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        cameraPreviewView.setLayoutParams(params);
        cameraPreviewView.setStickers(null,bitmap);
        cameraPreviewView.setFragmentShader(PROGRAM.CHARMING);
        rlContent.addView(cameraPreviewView);


        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnTakePhoto = findViewById(R.id.takePhoto);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);

        cameraPreviewView.setOnTakePhotoListener(this::saveBitmap);

        cameraPreviewView.switchCamera(1);
        cameraPreviewView.previewAngle(this);
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


        }
    }

    /**
     * 开始录制视频
     */
    private void start(){

        audioRecord = new AudioRecord();
        audioRecord.startRecord();
        mediaEncode = new MediaEncode(this,cameraPreviewView.getTextureId(),width,height);
        mediaEncode.initEncoder(cameraPreviewView.getEglContext(),
                Environment.getExternalStorageDirectory().getAbsolutePath()+"testabc.mp4",
                MediaFormat.MIMETYPE_VIDEO_AVC,960,540,44100,2);
        mediaEncode.setStickers(null,bitmap);
        mediaEncode.startRecord();

        audioRecord.setOnNativeCallbackPCMDataListener((buffer, size)
                -> mediaEncode.setPCMData(buffer,size));

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
        cameraPreviewView.takePhoto();
        cameraPreviewView.pausePreview();
        cameraPreviewView.playSoundEffect(this);
    }

    /**
     * 拍照之后保存图片
     * @param b
     */
    public void saveBitmap(Bitmap b){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/haha";

        long dataTake = System.currentTimeMillis();
        final String jpegName=path+ dataTake +".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
