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
 * @date 2020/8/18
 * @Describe:自定义View 1-2 Paint详解
 */
class ThirdView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    /**
     * Paint的API大致可以分为4类：
     * - 颜色
     * - 效果
     * - drawText()相关
     * - 初始化
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val mPaint = Paint().apply {

        /**
         * 1、Paint第一类API -- 关于颜色
         * 关于颜色有三层设置：
         * 1、直接设置颜色的API（基础颜色）用来给图形和文字设置颜色；
         * 2、setColorFilter()用来基于颜色进行过滤处理；
         * 3、setXfermode()用来处理源图像和View已有内容的关系。
         */

        /**
         * 1.1基础颜色
         *
         * 1、Canvas.drawColor()/ARGB() - 颜色参数
         * 2、Canvas.drawBitmap() - bitmap参数
         * 3、Canvas图形和文字绘制 - paint color参数
         */
        color = Color.CYAN

        /**
         * 1.2 ColorFilter 为绘制设置颜色过滤。
         *
         * ColorFilter并不直接使用，而是使用它的子类。一共有三个子类：
         * LightingColorFilter、PorterDuffColorFilter和ColorMatrixColorFilter。
         */
        /**
         *  1.2.1 LightingColorFilter
         *
         * LightingColorFilter是用来模拟简单的光照效果的。构造方法是：
         * LightingColorFilter(int mul,int add)
         * 参数里mul和add都是和颜色值格式相同的int值，其中mul用来和目标像素相乘，
         * add用来和目标像素相加：
         * R` = R * mul.R / 0xff + add.R
         * G` = G * mul.G / 0xff + add.G
         * B` = B * mul.B / 0xff + add.B
         * 一个'保持原样'的基本LightingColorFilter，mul为0xffffff，add为0x000000（也就是0）。
         * 基于这个基本LightingColorFilter，可以修改一下其他的filter，比如
         * 去掉像素中的红色，可以把mul改为0x00ffff（红色部分为0）；如果想绿色更
         * 亮一些，可以把add改为 0x003000（绿色部分为0x30）。
         */
//        colorFilter = LightingColorFilter(0x00ffff,0x003000)
        /**
         * 1.2.2 PorterDuffColorFilter
         * PorterDuffColorFilter的作用是使用一个指定的颜色和一种指定的
         * PorterDuff.Mode来绘制对象进行合成。它的构造方法是：
         * PorterDuffColorFilter(int color,PorterDuff.Mode mode)
         * color参数是指定的颜色；
         * mode参数是指定的PorterDuff.Mode。
         * 此处的mode和ComposeShader不同的是，PorterDuffColorFilter作为
         * 一个ColorFilter，只能指定一种颜色作为源，而不能是一个Bitmap。
         */
//        colorFilter = PorterDuffColorFilter(Color.BLUE,PorterDuff.Mode.OVERLAY)

        /**
         * 1.2.3 ColorMatrixColorFilter
         *
         * ColorMatrixColorFilter使用一个ColorMatrix来对颜色进行处理。
         * ColorMatrix这个类，内部是一个 4x5的矩阵：
         * [ a, b, c, d, e,
         *   f, g, h, i, j,
         *   k ,l, m, n, o,
         *   p, q, r, s, t ]
         * 通过计算，ColorMatrix可以把要绘制的像素进行转换。对于颜色[R,G,B,A]转换算法如下：
         * R` = a*R + b*G + c*B + d*A + e;
         * G` = f*R + g*G + h*B + i*A + j;
         * B` = k*R + l*G + m*B + n*A + o;
         * A` = p*R + q*G + r*B + s*A +t;
         *
         * 构造方法：
         * - ColorMatrixColorFilter(float[] array)
         * - ColorMatrixColorFilter(ColorMatrix matrix)
         *
         * ColorMatrix有一些自带的方法可以做简单的转换，例如可以使用setSaturation(float sat)
         * 来设置饱和度；另外也可以自己设置它的每一个元素来对转换效果做精细调整。
         */
        val array = floatArrayOf(
                0.5f,0f,0f,0f,0f,
                0f,1f,0f,0f,0f,
                0f,0f,1f,0f,0f,
                0f,0f,0f,1f,0f
        )
        val colorMatrix = ColorMatrix(array).apply {
            //设置饱和度
            setSaturation(0.5f)
        }
        colorFilter = ColorMatrixColorFilter(colorMatrix)

        /**
         * 1.3 Xfermode 处理'当颜色遇上View'的问题
         *
         * - setXfermode(Xfermode xfermode)
         * 'Xfermode'其实就是'Transfer mode'，用'X'来代替'Trans'是一种简写方式。
         * 严谨来说，Xfermode指的是要绘制的内容和Canvas的目标位置的内容应该怎样
         * 结合计算出最终的颜色；
         * 通俗的说，其实就是要你以绘制的内容作为源图像，以View中已有的内容作为目标图像，
         * 选取一个PorterDuff.Mode作为绘制内容的颜色处理方案。
         */
//        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)



        /**
         * 2、Paint第二类API -- 效果
         *
         * 效果类的API，指的就是抗锯齿、填充/轮廓、线条宽度等等。
         */
        /**
         * 2.1 抗锯齿 setAntiAlias()
         * 抗锯齿默认是关闭的，需要时，要显式打开。
         * 除了setAntiAlias(aa)方法，也可以在创建Paint对象时，构造
         * 方法的参数中设置：
         * Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
         */
        isAntiAlias = true
        /**
         * 2.2 填充效果 setStyle(Paint.Style style)
         * 用来设置图形是线条风格还是填充风格（也可以二者并用）
         * Paint.Style.FILL：填充
         * Paint.Style.STROKE：线条
         * Paint.Style.FILL_AND_STROKE：二者并用
         * 默认是Paint.Style.FILL。
         */
        style = Paint.Style.STROKE

        /**
         * 2.3 线条形状
         * - setStrokeWidth(float width)
         *   设置线条宽度，单位为像素，默认值是0。线条宽度0和1的区别：
         *   默认情况下，线条宽度为0，这个时候依然能够画出线，线条的宽度为1像素。
         *   线条宽度为1时，在Canvas通过Matrix来实现几何变换时，宽度也会跟随
         *   变化，但线条宽度为0时固定为1像素。
         *
         * - setStrokeCap(Paint.Cap cap)
         *   设置线头的形状。线头形状有三种：BUTT 平头、ROUND 圆头、SQUARE 方头。
         *   默认为BUTT。
         *
         * - setStrokeJoin(Paint.Join join)
         *   设置拐角的形状。有三个值：MITER 尖角、BEVEL 平角、ROUND 圆角。
         *   默认为MITER。
         *
         * - setStrokeMiter(float miter)
         *   这个方法是对于setStrokeJoin()的一个补充，它用于设置MITER型
         *   拐角的延长线的最大值。
         *   当线条拐角为MITER时，拐角处的外缘需要使用延长线来补偿；而这种补偿
         *   方案会有一个问题：如果拐角的角度太小，就有可能由于出现连接点过长的情况。
         *   所以为了避免意料之外的过长尖角出现，MITER型连接点有一个额外的规则：
         *   当尖角过长时，自动改为用BEVEL的方式来渲染连接点。至于多尖的角属于过尖，
         *   尖到需要转换为使用BEVEL来绘制，则是由一个属性控制的，这个属性就是
         *   setStrokeMiter(float miter)方法中的miter参数。
         *   miter参数是对于转角长度的限制，具体的是指尖角的外缘端点和内部拐角的距离与
         *   线条宽度的比。
         *
         *   如果拐角大小为θ，那么这个比值就等于1 / sin ( θ / 2 ) 。
         *   这个miter limit的默认值是4，对应的是一个大约29度的锐角；
         *   默认情况下，大于这个角的尖角会被保留，而小于这个夹角的就会被削成平角。
         *
         */
        strokeWidth = ResourceUtil.getDimen(context,R.dimen.dp_3)
        //设置线头形状
        strokeCap = Paint.Cap.ROUND
        //设置拐角形状
        strokeJoin = Paint.Join.ROUND


        /**
         * 2.4 色彩优化
         *
         * Paint的色彩优化有两个方法：setDither(boolean dither)和
         * setFilterBitmap(boolean filter).
         * 它们的作用都是让画面颜色变得更加'顺眼'，但原理和使用场景不同。
         * setDither(dither)设置抖动来优化色彩深度降低时的绘制效果；
         * setFilterBitmap(filterBitmap)设置双线性过滤来优化Bitmap放大绘制的效果。
         */

        /**
         * 2.4.1 setDither(boolean dither) 设置图像的抖动。
         * 抖动：所谓抖动，是指把图像从较高色彩深度（即可用的颜色数）
         *      向较低色彩深度的区域绘制时，在图像中有意的插入噪点，
         *      通过有规律地扰乱图像来让图像对于肉眼更加真实的做法。
         *
         * 现在的Android版本的绘制，默认的色彩深度已经是32位的ARGB_8888
         * 效果已经足够清晰了。只有当向自建的Bitmap中绘制，并且选择
         * 16位色的ARGB_4444或者RGB_565的时候，开启它才会有比较明显的效果。
         */
        isDither = true
        /**
         * 2.4.2 setFilterBitmap(boolean filter)
         * 设置是否使用双线性过滤来绘制Bitmap。
         * 图像在放大绘制的时候，默认使用的是最近邻插值过滤，这种算法简单，
         * 但是会出现马赛克现象；如果开启双线性过滤，就可以让结果图像显得
         * 更加平滑。
         */
        isFilterBitmap = true

        /**
         * 2.5 轮廓效果设置
         * - setPathEffect(PathEffect effect)
         * 使用PathEffect来给图形的轮廓设置效果，对Canvas所有的图形绘制有效。
         * 也就是drawLine(),drawCircle(),drawPath()这些方法都有效。
         *
         * Android中有6中PathEffect，可以分为两类：单一效果和组合效果。
         * 单一效果：
         * CornerPathEffect,DiscretePathEffect,DashPathEffect,PathDashPathEffect
         * 组合效果：
         * SumPathEffect,ComposePathEffect.
         *
         * 2.5.1 CornerPathEffect：把所有拐角变成圆角。
         * 构造方法CornerPathEffect(float radius)
         * radius参数是圆角的半径。
         *
         * 2.5.2 DiscretePathEffect：把线条进行随机的偏离。
         * DiscretePathEffect具体的做法是，把绘制改为使用定长的线段来拼接，
         * 并且在拼接的时候对路径进行随机偏离。
         * 构造方法DiscretePathEffect(float segmentLength,float deviation)
         * segmentLength：用来拼接的每个线段的长度
         * deviation：是偏离量。
         *
         * 2.5.3 DashPathEffect：使用虚线来绘制线条。
         * 构造方法DashPathEffect(float[] intervals,float phase)
         * intervals：指定了虚线的格式，数组中的元素必须为偶数（最少2个），
         * 按照 '画线长度、空白长度、画线长度、空白长度'的顺序排列。
         * phase：虚线的偏移量
         *
         * 2.5.4 PathDashPathEffect：使用path绘制的图形来绘制虚线。
         * 构造方法：
         * PathDashPathEffect(Path shape, float advance, float phase, PathDashPathEffect.Style style)
         * shape：用来绘制的Path；
         * advance：两个相邻shape之间的间隔，这个间隔是两个shape起点的间隔，而不是前一个终点到后一个起点的间隔；
         * phase：虚线的偏移量；
         * style：指定拐弯改变的时候shape的转换方式。
         *
         * PathDashPathEffect.Style是一个enum，具体有是那个值：
         * - TRANSLATE：位移
         * - ROTATE：旋转
         * - MORPH：变体
         *
         * 2.5.5 SumPathEffect 组合效果类的PathEffect。
         * 构造方法SumPathEffect(PathEffect first, PathEffect second)
         * 按照两种PathEffect分别对目标进行绘制。
         *
         * 2.5.6 ComposePathEffect 组合效果类的PathEffect
         * 构造函数ComposePathEffect(dashEffect, discreteEffect)
         * 它是先对目标path使用一个PathEffect，然后再对这个改变后的
         * path使用另一个PathEffect
         *
         */
        //DashPathEffect
//        pathEffect = DashPathEffect(floatArrayOf(50f,20f),10f)

        val shape = Path().apply {
//            addCircle(0f,0f,ResourceUtil.getDimen(context,R.dimen.dp_3),Path.Direction.CW)
            addArc(
                    0f,
                    0f,
                    ResourceUtil.getDimen(context,R.dimen.dp_3),
                    ResourceUtil.getDimen(context,R.dimen.dp_3),
                    -225f,
                    225f
            )
            arcTo(
                    ResourceUtil.getDimen(context,R.dimen.dp_3),
                    0f,
                    2f*ResourceUtil.getDimen(context,R.dimen.dp_3),
                    ResourceUtil.getDimen(context,R.dimen.dp_3),
                    -180f,
                    225f,
                    false
            )
            lineTo(
                    ResourceUtil.getDimen(context,R.dimen.dp_3),
                    2f*ResourceUtil.getDimen(context,R.dimen.dp_3)
            )
            color = Color.GREEN
        }
        //PathDashPathEffect
//        pathEffect = PathDashPathEffect(
//                shape,
//                ResourceUtil.getDimen(context,R.dimen.dp_10),
//                0f,
//                PathDashPathEffect.Style.ROTATE
//        )


        val dashEffect = DashPathEffect(floatArrayOf(50f,20f),10f)
        //DiscretePathEffect
        val discretePathEffect = DiscretePathEffect(20f,5f)
        //SumPathEffect
//        pathEffect = SumPathEffect(dashEffect,discretePathEffect)
        //ComposePathEffect
        pathEffect = ComposePathEffect(dashEffect,discretePathEffect)

        /**
         * 2.6 setShadowLayer(float radius,float dx,float dy,int shadowColor)
         * 在绘制内容下面添加一层阴影。
         * radius：阴影的模糊范围；
         * dx,dy：阴影的偏移量；
         * shadowColor：阴影颜色。
         *
         * 如果要清除阴影层，使用clearShadowLayer()。
         * 注意：在硬件加速开启的情况下，setShadowLayer()只支持文字的绘制；
         *      如果shadowColor是半透明的，阴影的透明度就使用shadowColor
         *      自己的透明度；而如果shadowColor是不透明的，阴影的透明度就
         *      使用paint的透明度。
         */
        setShadowLayer(10f,0f,0f,Color.YELLOW)

        /**
         * 2.7 setMaskFilter(MaskFilter maskFilter)
         * 为绘制设置MaskFilter。上一个方法setShadowLayer()是设置在绘制层
         * 下方的附加效果；而这个MaskFilter和它相反，设置的是在绘制层上方的附加效果。
         *
         * MaskFilter有两种：
         * BlurMaskFilter和EmbossMaskFilter。
         *
         * 2.7.1 BlurMaskFilter：模糊效果的MaskFilter。
         * 构造方法BlurMaskFilter(float radius,BlurMaskFilter.Blur style)
         * radius：模糊的范围；
         * style：模糊的类型。一共有四种：
         * - NORMAL：内外都模糊绘制
         * - SOLID：内部正常绘制，外部模糊
         * - INNER：内部模糊，外部不绘制
         * - OUTER：内部不绘制，外部模糊
         *
         * 2.7.2 EmbossMaskFilter：浮雕效果的MaskFilter
         * 构造方法EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius)
         * direction：是一个3个元素的数组，指定了光源的方向；
         * ambient：是环境光的强度，范围是0到1；
         * specular：是炫光的系数；
         * blurRadius：是应用光线的范围。
         */
//        maskFilter = BlurMaskFilter(
//                ResourceUtil.getDimen(context,R.dimen.dp_5),
//                BlurMaskFilter.Blur.NORMAL
//        )

        /**
         * 2.8 获取绘制的Path
         *
         */
    }
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    private val bitmap1 = BitmapFactory.decodeResource(context?.resources,R.drawable.img_j1)
    private val bitmap2 = BitmapFactory.decodeResource(context?.resources,R.drawable.img_j2,
            BitmapFactory.Options().also {
            it.inPreferredConfig = Bitmap.Config.RGB_565
    })
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.WHITE)
//        招商银行S020596王小翠 18611521294
        /**
         * 画Bitmap
         * - drawBitmap(Bitmap bitmap,float left,float top,Paint paint)
         * left和top是bitmap绘制的位置坐标。
         *
         * - drawBitmap(Bitmap bitmap,Rect src,RectF dst,Paint paint)
         * - drawBitmap(Bitmap bitmap,Rect src,Rect dst,Paint paint)
         * 第一个参数为要绘制的bitmap对象，第二个参数为要绘制的Bitmap对象的矩形区域，
         * 第三个参数为要将bitmap绘制在屏幕的什么地方
         *
         * - drawBitmap(Bitmap bitmap,Matrix matrix,Paint paint)
         * matrix：绘制bitmap时用于变换的矩阵
         *
         *
         * 绘制Bitmap对象，就是把这个Bitmap中的像素内容贴过来。
         *
         *
         * drawBitmap()还有一个兄弟方法drawBitmapMesh()，可以绘制具有网格拉伸
         * 效果的Bitmap。
         */

        //做短暂的离屏缓冲
        val saved = canvas?.saveLayer(
                null,
                null,
        Canvas.ALL_SAVE_FLAG)?:1

        bitmap1?.let {
            val bWidth = it.width
            val bHeight = it.height

            val matrix = Matrix().apply {
                //设置缩放
                setScale(measuredWidth.toFloat()/bWidth/2f,measuredHeight.toFloat()/bHeight/2f)
                //设置起点
                postTranslate(0f,0f)
            }
            canvas?.drawBitmap(it,matrix,mPaint)
        }
        //设置Xfermode
        mPaint.xfermode = xfermode
        canvas?.drawCircle(
                measuredWidth.toFloat()/2f,
                measuredHeight.toFloat()/4f,
                measuredHeight.toFloat()/4f,
                mPaint
        )
        //使用完毕要及时清除Xfermode
        mPaint.xfermode = null
        canvas?.restoreToCount(saved)

        val path = Path().apply {
            moveTo(
                    ResourceUtil.getDimen(context,R.dimen.dp_10),
                    measuredHeight.toFloat()/2f + ResourceUtil.getDimen(context,R.dimen.dp_10))
            lineTo(
                    ResourceUtil.getDimen(context,R.dimen.dp_40),
                    measuredHeight.toFloat()/2f + ResourceUtil.getDimen(context,R.dimen.dp_10)
            )
            lineTo(
                    ResourceUtil.getDimen(context,R.dimen.dp_20),
                    measuredHeight.toFloat()/2f + ResourceUtil.getDimen(context,R.dimen.dp_30))
        }
        canvas?.drawPath(path,mPaint)


        bitmap2?.let {
            val bWidth = it.width
            val bHeight = it.height

            val matrix = Matrix().apply {
                //set()和post()的区别是:
                //set()相当于对matrix对象先重置reset(), 再施加形变操作.
                //因此post方法要在set之后调用
                //设置缩放
                setScale(measuredWidth.toFloat()/bWidth/2f,measuredHeight.toFloat()/bHeight/2f)
                //设置起点，需要先设置其他，在设置起点，否则会
                postTranslate(0f,measuredHeight.toFloat()/2f)
            }
            mPaint.maskFilter = BlurMaskFilter(
                    ResourceUtil.getDimen(context,R.dimen.dp_100),
                    BlurMaskFilter.Blur.OUTER
            )
            canvas?.drawBitmap(it,matrix,mPaint)
//            mPaint.maskFilter = null
        }

        canvas?.drawCircle(
                measuredWidth.toFloat()/4f,
                3f*measuredHeight.toFloat()/4f,
                measuredHeight.toFloat()/4 - ResourceUtil.getDimen(context,R.dimen.dp_10),
                mPaint
        )

        mPaint.pathEffect = null
        mPaint.strokeWidth = 0f
        /**
         * 绘制文字
         * - drawText(String text,float x,float y,Paint paint)
         * text：要绘制的字符串
         * x和y：绘制的起点坐标
         */
        mPaint.textSize = ResourceUtil.getDimen(context,R.dimen.sp_12)
        canvas?.drawText(
                "Hello HenCoder",
                measuredWidth.toFloat()/2f,
                measuredHeight.toFloat()/4f,
                mPaint
        )
    }

}