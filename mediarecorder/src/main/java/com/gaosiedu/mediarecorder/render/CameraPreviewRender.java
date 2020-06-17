package com.gaosiedu.mediarecorder.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.gaosiedu.mediarecorder.R;
import com.gaosiedu.mediarecorder.listener.OnTakePhotoListener;
import com.gaosiedu.mediarecorder.util.ImageTextureUtil;
import com.gaosiedu.mediarecorder.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class CameraPreviewRender extends BaseEGLRender implements SurfaceTexture.OnFrameAvailableListener {
    private final String TAG = "CameraPreviewRender";
    private final float[] vertex_data = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,

            -0.3f, -0.3f,
            0.3f, -0.3f,
            -0.3f, 0.3f,
            0.3f, 0.3f,

            0f, 0f,
            0f, 0f,
            0f, 0f,
            0f, 0f
    };

    private final float[] texture_data = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;


    private int program;
    private int avPosition;
    private int afPosition;
    private int u_matrix;
    private static final float[] matrix = ImageTextureUtil.getOriginMatrix();
    private static float[] rotateMatrix = ImageTextureUtil.getOriginMatrix();

    private int color;

    private int VBOId = 0;

    private Context context;

    /**
     * 是否变更了贴纸
     */
    private boolean changeSticker = false;
    /**
     * 是否变更了水印
     */
    private boolean changeWatermark = false;

    /**
     * 贴纸
     */
    private Bitmap sticker;
    /**
     * 水印
     */
    private Bitmap watermark;

    private int stickerTextureId = -1;
    private int watermarkTextureId = -1;

    private int width;
    private int height;


    private FloatBuffer colorBuffer;

    private boolean isTakePhoto = false;

    private OnTakePhotoListener onTakePhotoListener;


    public CameraPreviewRender(Context context,int width,int height) {
        this.context = context;
        this.width = width;
        this.height = height;
        vertexBuffer = ByteBuffer.allocateDirect(vertex_data.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex_data);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(texture_data.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texture_data);
        textureBuffer.position(0);

//        width = DisplayUtil.getScreenWidth(context);
//        height = DisplayUtil.getScreenHeight(context);

    }


    @Override
    protected void onCreated() {

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        String vertexSource = ShaderUtil.readRawText(context, R.raw.vertex2);
        String textureSource = ShaderUtil.readRawText(context, R.raw.fragment_preview);

        program = ShaderUtil.createProgram(vertexSource, textureSource);

        avPosition = GLES20.glGetAttribLocation(program, "v_Position");
        afPosition = GLES20.glGetAttribLocation(program, "f_Position");

        u_matrix = GLES20.glGetUniformLocation(program, "u_Matrix");

        bindVBO();

    }

    @Override
    protected void onChange(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onDrawFrame() {

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }

    void onDrawFrame(int FBOTextureId) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 0f, 0f, 1f);


        GLES20.glUseProgram(program);

        GLES20.glUniformMatrix4fv(u_matrix, 1, false, matrix, 0);

        //fbo
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, FBOTextureId);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
        GLES20.glEnableVertexAttribArray(avPosition);
        GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 0);
        GLES20.glEnableVertexAttribArray(afPosition);
        GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);


        //stickers

        if (changeSticker) {
            changeSticker = false;
            stickerTextureId = ImageTextureUtil.loadBitmapTexture2D(sticker);

            bindVBO();


        }

        //watermark
        if (changeWatermark) {
            changeWatermark = false;
            watermarkTextureId = ImageTextureUtil.loadBitmapTexture2D(watermark);

            bindVBO();

        }

        if (stickerTextureId != -1) {
            //sticker1
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, stickerTextureId);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
            GLES20.glEnableVertexAttribArray(avPosition);
            GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 32);
            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }

        if (watermarkTextureId != -1) {
            //watermark
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, watermarkTextureId);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
            GLES20.glEnableVertexAttribArray(avPosition);
            GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 32 * 2);
            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }

        if (isTakePhoto) {
            isTakePhoto = false;
            GLES20.glUniformMatrix4fv(u_matrix, 1, false, rotateMatrix, 0);

            //fbo
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, FBOTextureId);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
            GLES20.glEnableVertexAttribArray(avPosition);
            GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 0);
            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);


            //stickers

            if (changeSticker) {
                changeSticker = false;
                stickerTextureId = ImageTextureUtil.loadBitmapTexture2D(sticker);

                bindVBO();


            }


            if (changeWatermark) {
                changeWatermark = false;
                watermarkTextureId = ImageTextureUtil.loadBitmapTexture2D(watermark);

                bindVBO();

            }

            if (stickerTextureId != -1) {
                //sticker1
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, stickerTextureId);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
                GLES20.glEnableVertexAttribArray(avPosition);
                GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 32);
                GLES20.glEnableVertexAttribArray(afPosition);
                GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            }

            if (watermarkTextureId != -1) {
                //watermark
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, watermarkTextureId);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
                GLES20.glEnableVertexAttribArray(avPosition);
                GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 32 * 2);
                GLES20.glEnableVertexAttribArray(afPosition);
                GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            }


            ByteBuffer buffer = ByteBuffer.allocate(width * height * 4);
            GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
            if (onTakePhotoListener != null) {
                Log.d("CameraPreviewView","[onTakePhotoListener] width:"+width+",height:"+height);
                Log.d("CameraPreviewView","[onTakePhotoListener] size:"+buffer.array().length);
                onTakePhotoListener.onTake(buffer.array());
            }



            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }

    private void bindVBO() {

        if (VBOId != 0) {
//            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,VBOId);
//            GLES20.glDeleteBuffers(GLES20.GL_ARRAY_BUFFER,new int[]{VBOId},0);

            vertexBuffer = ByteBuffer.allocateDirect(vertex_data.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(vertex_data);
            vertexBuffer.position(0);

        }

        int[] vbos = new int[1];
        GLES20.glGenBuffers(1, vbos, 0);
        VBOId = vbos[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);

        GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                vertex_data.length * 4 + texture_data.length * 4,
                null,
                GLES20.GL_DYNAMIC_DRAW
        );

        GLES20.glBufferSubData(
                GLES20.GL_ARRAY_BUFFER,
                0,
                vertex_data.length * 4,
                vertexBuffer
        );

        GLES20.glBufferSubData(
                GLES20.GL_ARRAY_BUFFER,
                vertex_data.length * 4,
                texture_data.length * 4,
                textureBuffer
        );

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * 添加贴纸
     * @param sticker
     */
    public void addSticker(Bitmap sticker) {

        if (sticker == null) {
            stickerTextureId = -1;
            changeSticker = false;
            return;
        }

        vertex_data[8] = -1f;
        vertex_data[9] = -1f;

        vertex_data[10] = 1f;
        vertex_data[11] = -1f;

        vertex_data[12] = -1f;
        vertex_data[13] = 1f;

        vertex_data[14] = 1f;
        vertex_data[15] = 1f;


        this.sticker = sticker;
        this.changeSticker = true;
    }

    /**
     * 添加水印
     * @param watermark
     */
    public void addWatermark(Bitmap watermark) {

        if (watermark == null) {
            watermarkTextureId = -1;
            changeWatermark = false;
            return;
        }

        float scale = height * 1.0f / 720;

        float imgWidth = watermark.getWidth() * 1.0f*scale;
        float imgHeight = watermark.getHeight()*1.0f*scale;
        float marginTop = 40f*scale;
        float marginLeft = 130f*scale;

        float sw = imgWidth/width*2;
        float sh = imgHeight/height*2;
        float sMarginTop = marginTop/height*2;
        float sMarginLeft = marginLeft/width*2;

        vertex_data[16] = - 1f + sMarginLeft;
        vertex_data[17] = 1f - sMarginTop - sh;

        vertex_data[18] = -1 + sMarginLeft + sw;
        vertex_data[19] = 1f - sMarginTop - sh;

        vertex_data[20] = -1f + sMarginLeft;
        vertex_data[21] = 1f - sMarginTop;

        vertex_data[22] = -1f + sMarginLeft + sw;
        vertex_data[23] = 1f  - sMarginTop;

        this.watermark = watermark;
        this.changeWatermark = true;
    }


    public void setOnTakePhotoListener(OnTakePhotoListener onTakePhotoListener) {
        this.onTakePhotoListener = onTakePhotoListener;
    }

    public void takePhoto(int cameraId) {

//        Matrix.setIdentityM(rotateMatrix,0);
//
//        if (cameraId == 0) {
//            setRotateMatrix(180,1,0,0);
//        }else{
//            setRotateMatrix(180,0,0,1);
//            setRotateMatrix(180,0,1,0);
//        }
        isTakePhoto = true;
    }

    private void setRotateMatrix(float angle, float x, float y, float z) {
        Matrix.rotateM(rotateMatrix, 0, angle, x, y, z);
    }


}

