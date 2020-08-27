package com.wys.interview.android.henCoder.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R

/**
 * @author wangyasheng
 * @date 2020/8/24
 * @Describe:canvas裁切与转换
 */
class FiveView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    private val paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
    }
    private val path = Path()
    private val bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.img_j3)

    private val radius = ResourceUtil.getDimen(context,R.dimen.dp_50)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            /**
             * 1、范围裁切
             * clipRect()和clipPath()
             */
            it.save()
            it.clipRect(0f,0f,measuredWidth.toFloat()/2f,measuredHeight.toFloat()/2f)
            it.drawBitmap(bitmap,0f,0f,paint)
            it.restore()

            path.addCircle(3f*measuredWidth.toFloat()/4f,measuredHeight.toFloat()/2f,radius,Path.Direction.CW)
            it.save()
            it.clipPath(path)
            it.clipOutPath(path)
            it.drawBitmap(bitmap,0f,0f,paint)
            it.restore()

            /**
             * 2、几何变换
             * 几何变换的使用大概分为三类：
             * - 使用Canvas做常见的二维变换；
             * - 使用Matrix来做常见和不常见的二维变换；
             * - 使用Camera来做三位变换。
             */
            /**
             * 2.1 使用Canvas来做常见的二维变换：
             *
             * 2.1.1 Canvas.translate(float dx,float dy)平移。
             * dx，dy：表示横向和纵向的位移。
             *
             * 2.1.2 Canvas.rotate(float degrees,float px,float py)旋转
             * degrees：旋转的角度，单位度，方向顺时针；
             * px，py：轴心位置。
             *
             * 2.1.3 Canvas.scale(float sx,float sy,float px,float py)缩放
             * sx，sy：横向和纵向的缩放倍数；
             * px，py：是缩放的轴心。
             *
             * 2.1.4 Canvas.skew(float sx,float sy) 错切（倾斜）
             * sx，sy：是x方向和y方向的错切系数。
             */

            /**
             * 2.2 使用Matrix来做变换
             *
             * 2.2.1 使用Matrix做常见变换步骤：
             * - 创建Matrix对象；
             * - 调用Matrix对象的pre/postTranslate()/Rotate/Scale/Skew()方法设置几何变换；
             * - 使用Canvas.setMatrix(matrix)或者Canvas.concat(matrix)把几何变换应用到Canvas。
             *
             * Canvas.setMatrix(matrix)：用Matrix直接替换Canvas当前的变换矩阵；
             * Canvas.concat(matrix)：用Canvas当前的变换矩阵和Matrix相乘，即基于Canvas当前
             * 变换，叠加上matrix中的变换。
             *
             *
             * 2.2.2 使用Matrix来做自定义变换
             * Matrix的自定义变换使用的是setPolyToPoly()方法。
             *
             * 2.2.2.1 Matrix.setPolyToPoly(float[] src,int srcIndex,float[] dst,int dstIndex, int pointCount)
             * 用点对点映射的方式设置变换。
             * src，dst：是源点集和目标点集；
             * srcIndex,dstIndex：是第一个点的偏移；
             * pointCount：是采集的点的个数（个数不能大于4，因为大于4个点无法计算变换）
             */

            /**
             * 2.3 使用Camera做三维变换
             * Camera的三维变换有三类：旋转、平移、移动相机。
             *
             * 2.3.1 Camera.rotate*()三维旋转
             * Camera.rotate*()一共有四个方法：
             * - rotateX(deg)
             * - rotateY(deg)
             * - rotateZ(deg)
             * - rotate(x,y,z)
             *Camera和Canvas一样也需要保存和恢复状态才能正常绘制，不然在界面
             * 刷新之后绘制就会出现问题。
             * Canvas的几何变换顺序是反的。
             *
             * 2.3.2 Camera.translate(float x, float y,float z)移动
             *
             * 2.3.3 Camera.setLocation(x,y,z)设置虚拟相机的位置。
             * 参数的单位不是像素，而是inch（英尺）。
             */
            val matrix = Matrix()
            val pointSrc = floatArrayOf(
                    0f,0f,
                    bitmap.width.toFloat(),0f,
                    0f,bitmap.height.toFloat(),
                    bitmap.width.toFloat(),bitmap.height.toFloat()
            )
            val pointDst = floatArrayOf(
                    measuredWidth.toFloat()/4f,measuredHeight.toFloat()/4f,
                    measuredWidth.toFloat(),0f,
                    measuredWidth.toFloat()/4f,3f*measuredHeight.toFloat()/4f,
                   3f* measuredWidth.toFloat()/4f,3f*measuredHeight.toFloat()/4f
            )
//            val pointDst = floatArrayOf(
//                    0f,0f,
//                    measuredWidth.toFloat(),0f,
//                    0f,measuredHeight.toFloat(),
//                    measuredWidth.toFloat(),measuredHeight.toFloat()
//            )
            matrix.reset()
            matrix.setPolyToPoly(pointSrc,0,pointDst,0,4)
            it.save()
            it.concat(matrix)
            it.drawBitmap(bitmap,0f,0f,paint)
            it.restore()
            val camera = Camera()
            it.save()
            matrix.reset()
            matrix.setScale(measuredWidth.toFloat()/bitmap.width .toFloat()/2f,
                        measuredHeight.toFloat()/bitmap.height.toFloat()/2f,
                        0f,
                        0f
                )
            it.setMatrix(matrix)
            //保存Camera的状态
            camera.save()
            //旋转Camera的三维空间
            camera.rotateX(30f)
            //旋转之后把投影移动回来
            it.translate(measuredWidth.toFloat()/2f,measuredHeight.toFloat()/2f)
            //把旋转投影到Canvas
            camera.applyToCanvas(it)
            //旋转之前把绘制的内容移动到轴心（原点）
            it.translate(- measuredWidth.toFloat()/2f,- measuredHeight.toFloat()/2f)
           //恢复Camera的状态
            camera.restore()

            it.drawBitmap(bitmap,
                    0f,
                    0f,
                    paint
            )
            it.restore()
        }
    }
}