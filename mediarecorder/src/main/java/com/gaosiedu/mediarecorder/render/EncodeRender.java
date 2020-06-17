package com.gaosiedu.mediarecorder.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.gaosiedu.mediarecorder.R;
import com.gaosiedu.mediarecorder.util.DisplayUtil;
import com.gaosiedu.mediarecorder.util.ImageTextureUtil;
import com.gaosiedu.mediarecorder.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class EncodeRender extends BaseEGLRender {

    private final float[] vertex_data = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,

            0f, 0f,
            0f, 0f,
            0f, 0f,
            0f, 0f,

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

    private Context context;


    private int program;
    private int avPosition;
    private int afPosition;
    private int u_matrix;
    private float[] matrix = ImageTextureUtil.getOriginMatrix();


    private int VBOId;

    private int textureId;

    private boolean changeSticker = false;
    private boolean changeWatermark = false;

    private boolean changeFragmentShader = false;
    private int fragmentShader = R.raw.fragment_preview;

    private Bitmap sticker;
    private Bitmap watermark;

    private int stickerTextureId = -1;
    private int watermarkTextureId = -1;

    private int width;
    private int height;

    public EncodeRender(Context context, int textureId,int width,int height) {

        this.context = context;
        this.textureId = textureId;
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
    public void onSurfaceCreated() {

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);


        String vertexSource = ShaderUtil.readRawText(context, R.raw.vertex2);
        String textureSource = ShaderUtil.readRawText(context, fragmentShader);

        program = ShaderUtil.createProgram(vertexSource, textureSource);

        avPosition = GLES20.glGetAttribLocation(program, "v_Position");
        afPosition = GLES20.glGetAttribLocation(program, "f_Position");
        u_matrix = GLES20.glGetUniformLocation(program,"u_Matrix");

        int[] vbos = new int[1];
        GLES20.glGenBuffers(1,vbos,0);
        VBOId = vbos[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,VBOId);

        GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                vertex_data.length * 4 + texture_data.length * 4,
                null,
                GLES20.GL_STATIC_DRAW
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

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    protected void onCreated() {

    }

    @Override
    protected void onChange(int width, int height) {

    }

    @Override
    public void onDrawFrame() {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0f, 0f, 1f);

        GLES20.glUseProgram(program);

        GLES20.glUniformMatrix4fv(u_matrix,1,false, matrix,0);

        //fbo
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,VBOId);

        GLES20.glEnableVertexAttribArray(avPosition);
        GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 0);
        GLES20.glEnableVertexAttribArray(afPosition);
        GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);


        //stickers

        if(changeSticker){
            changeSticker = false;
            stickerTextureId = ImageTextureUtil.loadBitmapTexture2D(sticker);

            bindVBO();


        }


        if(changeWatermark){
            changeWatermark = false;
            watermarkTextureId = ImageTextureUtil.loadBitmapTexture2D(watermark);

            bindVBO();

        }

        //sticker1
        if(stickerTextureId != -1) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, stickerTextureId);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
            GLES20.glEnableVertexAttribArray(avPosition);
            GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 32);
            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }

        //watermark
        if(watermarkTextureId != -1){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, watermarkTextureId);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOId);
            GLES20.glEnableVertexAttribArray(avPosition);
            GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, 32 * 2);
            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, vertex_data.length * 4);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }

    private void bindVBO() {

        if(VBOId != 0) {
//            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,VBOId);
//            GLES20.glDeleteBuffers(GLES20.GL_ARRAY_BUFFER,new int[]{VBOId},0);

            vertexBuffer = ByteBuffer.allocateDirect(vertex_data.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(vertex_data);
            vertexBuffer.position(0);

        }

        int[] vbos = new int[1];
        GLES20.glGenBuffers(1,vbos,0);
        VBOId = vbos[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,VBOId);

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

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
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
        vertex_data[23] = 1f - sMarginTop;

        this.watermark = watermark;
        this.changeWatermark = true;
    }


    public void setFBOId(int id){
        this.textureId = id;
    }
}
