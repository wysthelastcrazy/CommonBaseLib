package com.gaosiedu.mediarecorder.util;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

public class CameraUtil {



    public static Camera.Size getCameraSize(Context context){

        float rate = 16*1.0f/9;
        Camera camera = Camera.open();

        for(int i = 0; i < camera.getParameters().getSupportedPreviewSizes().size();i++){

            Log.e(
                    "camera",
                    String.format("width is %d, height is %d , screen height is %d",
                            camera.getParameters().getSupportedPreviewSizes().get(i).width,
                            camera.getParameters().getSupportedPreviewSizes().get(i).height,
                            1
                    )
            );

//            if(true){
//                continue;
//            }

            Camera.Size size = camera.getParameters().getSupportedPreviewSizes().get(i);

//            if(size.height * 1.0f / size.width ==  9 * 1.0f / 16){
//                camera.release();
//                return size;
//            }
            if (size.height>=720&&equalRate(size,rate)){
                camera.release();
                return size;
            }


        }

        Camera.Size size = camera.getParameters().getSupportedPreviewSizes().get(0);

        camera.release();

        return size;
    }
    private static boolean equalRate(Camera.Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.03) {
            return true;
        }else{
            return false;
        }
    }

}
