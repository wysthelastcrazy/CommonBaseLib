package com.gaosiedu.mediarecorder.encoder;

import android.content.Context;
import android.graphics.Bitmap;

import com.gaosiedu.mediarecorder.render.BaseEGLRender;
import com.gaosiedu.mediarecorder.render.EncodeRender;


public class MediaEncode extends BaseMediaEncoder {

    private EncodeRender encodeRender;


    public MediaEncode(Context context, int textureId,int width,int height) {
        super(context);
        encodeRender = new EncodeRender(context,textureId,width,height);
        setRender(encodeRender);
        setRenderMode(RenderMode.RENDER_MODE_CONTINUOUSLY);
    }


    public void setSticker(Bitmap sticker){
        ((EncodeRender)encodeRender).addSticker(sticker);

    }
    public void setWatermark(Bitmap watermark){
        ((EncodeRender)encodeRender).addWatermark(watermark);
    }

    public void setFBOTextureId(int textureId){
        encodeRender.setFBOId(textureId);
    }

}
