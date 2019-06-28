package com.wys.audio_video_editor.camera;

import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.wys.audio_video_editor.utils.OpenGLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by yas on 2019/6/26
 * Describe:
 */
public class DirectDrawer {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec2 inputTextureCoordinate;" +
                    "varying vec2 textureCoordinate;" +
                    "void main()" +
                    "{"+
                    "gl_Position = vPosition;"+
                    "textureCoordinate = inputTextureCoordinate;" +
                    "}";

    private final String fragmentShaderCode =
            "#extension GL_OES_EGL_image_external : require\n"+
                    "precision mediump float;" +
                    "varying vec2 textureCoordinate;\n" +
                    "uniform samplerExternalOES s_texture;\n" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                    "}";
//    private final String fragmentShaderCode = "#extension GL_OES_EGL_image_external:require\n" +
//        "precision mediump float;\n" +
//        "varying vec2 textureCoordinate;\n" +
//        "uniform samplerExternalOES s_texture;\n" +
//        "float colorMatrix[20];\n" +
//        "void main(){\n" +
//        "\n" +
//        "    colorMatrix[0] = 0.8;\n" +
//        "    colorMatrix[1] = 0.3;\n" +
//        "    colorMatrix[2] = 0.1;\n" +
//        "    colorMatrix[3] = 0.0;\n" +
//        "    colorMatrix[4] = 46.5 / 255.0;\n" +
//        "\n" +
//        "\n" +
//        "    colorMatrix[5] = 0.1;\n" +
//        "    colorMatrix[6] = 0.9;\n" +
//        "    colorMatrix[7] = 0.0;\n" +
//        "    colorMatrix[8] = 0.0;\n" +
//        "    colorMatrix[9] = 46.5 / 255.0;\n" +
//        "\n" +
//        "    colorMatrix[10] = 0.1;\n" +
//        "    colorMatrix[11] = 0.3;\n" +
//        "    colorMatrix[12] = 0.7;\n" +
//        "    colorMatrix[13] = 0.0;\n" +
//        "    colorMatrix[14] = 46.5 / 255.0;\n" +
//        "\n" +
//        "    colorMatrix[15] = 0.0;\n" +
//        "    colorMatrix[16] = 0.0;\n" +
//        "    colorMatrix[17] = 0.0;\n" +
//        "    colorMatrix[18] = 1.0;\n" +
//        "    colorMatrix[19] = 0.0;\n" +
//        "\n" +
//        "\n" +
//        "\n" +
//        "    vec4 rgb = texture2D(s_texture,textureCoordinate);\n" +
//        "\n" +
//        "    float rn = rgb.r * colorMatrix[0 + 5 * 0] + rgb.g * colorMatrix[1 + 5 * 0] + rgb.b * colorMatrix[2 + 5 * 0] + rgb.a * colorMatrix[3 + 5 * 0] + colorMatrix[4 + 5 * 0];\n" +
//        "    float gn = rgb.r * colorMatrix[0 + 5 * 1] + rgb.g * colorMatrix[1 + 5 * 1] + rgb.b * colorMatrix[2 + 5 * 1] + rgb.a * colorMatrix[3 + 5 * 1] + colorMatrix[4 + 5 * 1];\n" +
//        "    float bn = rgb.r * colorMatrix[0 + 5 * 2] + rgb.g * colorMatrix[1 + 5 * 2] + rgb.b * colorMatrix[2 + 5 * 2] + rgb.a * colorMatrix[3 + 5 * 2] + colorMatrix[4 + 5 * 2];\n" +
//        "    float an =   0.0 * colorMatrix[0 + 5 * 3] +   0.0 * colorMatrix[1 + 5 * 3] +   0.0 * colorMatrix[2 + 5 * 3] +   1.0 * colorMatrix[3 + 5 * 3] + colorMatrix[4 + 5 * 3];\n" +
//        "\n" +
//        "    gl_FragColor = vec4(rn,gn,bn,an);\n" +
//        "\n" +
//        "}";

    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mTextureCoordHandle;

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    private static final int COORDS_PER_VERTEX = 2;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    static float squareCoords[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f,  1.0f,
    };

    static float textureVertices[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    private int texture;

    private int stickerTextureId;

    public DirectDrawer(int texture)
    {
        this.texture = texture;
        //顶点坐标
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
        //顶点绘制顺序
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        //纹理坐标
        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);
        //编译着色器
        int vertexShader    = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader  = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }
    public void draw() {
        GLES20.glUseProgram(mProgram);
        //使用纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);
        //顶点位置
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        //纹理坐标
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureVerticesBuffer);
        //绘制
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        //结束
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
    }

    //编译着色器
    private  int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void setSticker(Bitmap sticker){
        stickerTextureId = OpenGLUtil.loadBitmapTexture2D(sticker);
    }
}
