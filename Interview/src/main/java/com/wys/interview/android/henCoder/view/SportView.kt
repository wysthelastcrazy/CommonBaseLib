package com.wys.interview.android.henCoder.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R
import kotlinx.android.synthetic.main.activity_view.*

/**
 * @author wangyasheng
 * @date 2020/8/25
 * @Describe:属性动画
 */
class SportView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    /**
     * 1、ViewPropertyAnimator
     * 使用方式：View.animate()后跟相应的方法，动画会自动执行。
     * 例如：view.animate().translationX(500f)
     * 有一系列相关方法，其中带有By后缀的表示动画把View相关值渐变增加指定的值；
     * 不带By后缀的则表示把View相关值渐变到指定值。
     */

    /**
     * 2、ObjectAnimator
     * 使用方式：
     * (1)、如果是自定义控件，需要添加setter/getter方法；
     * (2)、用ObjectAnimator.ofXXX()创建ObjectAnimator对象；
     * (3)、用start()方法执行动画。
     */

    /**
     * 3、通用功能
     **/
    /**
     * 3.1 setDuration(int duration)设置动画时长，单位毫秒。
     * view.animate().translationX(500).setDuration(500);
     *
     * ObjectAnimator animator = ObjectAnimator.ofFloat(view,"translationX",500);
     * animator.setDuration(500);
     * animator.start();
     **/
    /**
     *  3.2 setInterpolator(Interpolator interpolator)设置Interpolator。
     * Interpolator就是速度设置器。设置不同的Interpolator，动画就会以不同的速度模型来执行。
     *
     * 3.2.1 AccelerateDecelerateInterpolator
     * 现加速再减速，这是默认的Interpolator。
     * 用途：它是一种最符合物理世界的模型，所以如果要做的是最简单的状态
     * 变化（位移、缩放、旋转等等），就用这个默认的最好。
     *
     * 3.2.2 LinearInterpolator 匀速。
     *
     * 3.2.3 AccelerateInterpolator 持续加速，结束时直接停止。
     * 用途：主要用在离场效果中，比如某个物体从界面中飞离。
     *
     * 3.2.4 DecelerateInterpolator 持续减速直到0。
     * 用途：主要用于入场效果。
     *
     * 3.2.5 AnticipateInterpolator 先拉回一下在进行正常的动画轨迹。
     * 效果看起来有点像投掷物或跳跃等动作前的蓄力。
     *
     * 3.2.6 OvershootInterpolator 动画会超过目标值一些，然后再回弹。
     *
     * 3.2.7 AnticipateOvershootInterpolator 开始前回拉，最后回弹。
     *
     * 3.2.8 BounceInterpolator 在目标处弹跳。
     * 效果类似弹珠掉在地板上的感觉。
     *
     * 3.2.9 CycleInterpolator
     * 这个也是一个正弦/余弦曲线，不过和AccelerateDecelerateInterpolator
     * 的区别是：它可以自定义曲线的周期，所以动画可以不到终点就结束，也可以到达终点
     * 后回弹，回弹次数由曲线的周期决定，曲线周期由CycleInterpolator的构造方法
     * 的参数决定。
     *
     * 3.2.10 PathInterpolator 自定义动画完成度/时间完成度曲线。
     * 用这个Interpolator可以定制处任何想要的速度模型。定制的方式是使用一个Path
     * 对象来绘制处想要的动画完成度/时间完成度曲线。
     * 这条Path描述的其实是一个 y = f(x)(0<= x <= 1)的曲线，y为动画完成度，x为
     * 时间完成度。所以同一段时间完成度上不能有两段不同的动画完成度，而且每一个时间
     * 完成度的点上都必须有对应的动画完成度。
     *
     *
     * 除上述这些Interpolator之外，Android 5.0（API 21）引入了三个新的Interpolator。
     * 这是三个新的Interpolator每个都和之前的某个已有的Interpolator规则相似，
     * 只有略微区别。
     *
     * 3.2.11 FastOutLinearInInterpolator 持续加速运动。
     * 它和AccelerateInterpolator一样，都是一个持续加速的运动路线，区别就是
     * FastOutLinearInInterpolator的曲线公式是用的贝塞尔曲线，而AccelerateInterpolator
     * 用的是指数曲线。因此FastOutLinearInInterpolator的初始阶段加速度比
     * AccelerateInterpolator要快一些。
     *
     * 3.2.12 FastOutSlowInInterpolator 现加速再减速。
     * AccelerateDecelerateInterpolator指数曲线；
     * FastOutSlowInInterpolator贝塞尔曲线。FastOutSlowInInterpolator的前期加速度要快。
     *
     * 3.2.13 LinearOutSlowInInterpolator 持续减速。
     * 与DecelerateInterpolator区别和上述两个同理。
     */

    /**
     * 4、ValueAnimator 最基本的轮子
     * 很多使用，用不到它。在想要做动画的属性却没有setter/getter方法的时候，才会用到。
     * ValueAnimator是ObjectAnimator的父类，实际上ValueAnimator就是一个不能
     * 指定目标对象的ObjectAnimator。ObjectAnimator是自动调用目标对象的setter方法
     * 来更新目标属性的值，以及跟多时候还会一次来改变目标对象的UI。而ValueAnimator
     * 只是通过渐变的方式来改变一个独立的数据，这个数据不是属于某个对象的，至于在数据
     * 更新后要做什么是，全都有自己定义。
     */

    /**
     * 总结
     * ViewPropertyAnimator、ObjectAnimator、ValueAnimator这三种Animator，
     * 它们其实是一种递进的关系：从左到右一次变得更加难用，也更加灵活。它们的性能是一样的，
     * 因为ViewPropertyAnimator和ObjectAnimator的内部实现其实都是ValueAnimator。
     * 它们的差别只是使用的便捷性以及功能的灵活性。
     * 所以在实际使用的时候，尽量用简单的。
     */
    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = ResourceUtil.getDimen(context, R.dimen.dp_5)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }
    private val textPaint = TextPaint().apply {
        textSize = ResourceUtil.getDimen(context,R.dimen.sp_14)
        color = Color.BLACK
        isAntiAlias = true
    }
    private var progress = 0f
        set(value){
            field = value
            invalidate()
        }
    private var color = Color.GREEN
         set(value) {
             field = value
             paint.color = field
         }
@SuppressLint("ObjectAnimatorBinding")
fun startAnim(start:Float, end:Float){
        val animator = ObjectAnimator.ofFloat(this,"progress",start,end)
        animator.duration = 2000L
        animator.interpolator = AccelerateDecelerateInterpolator()
//        animator.start()

        /**
         * 3.3 设置监听器
         */
        /**
         * 3.3.1 ViewPropertyAnimator.setListener()/ObjectAnimator.addListener()
         * 这个两方法的名称不一样，可以设置的监听数量也不一样，但是它们的参数都是
         * AnimatorListener，所以本质上其实都是一样的。
         */
        animator.addListener(animatorListener)
//        animate().setListener(animatorListener)
        /**
         * 3.3.2 setUpdateListener()/addUpdateListener()
         * 参数都为AnimatorUpdateListener
         */
        animator.addUpdateListener(animatorUpdateListener)
//        animate().setUpdateListener(animatorUpdateListener)
        /**
         * 3.3.3 ObjectAnimator.addPauseListener()
         * 此方法为ObjectAnimator独有方法。
         */
        animator.addPauseListener(object :Animator.AnimatorPauseListener{
            override fun onAnimationPause(animation: Animator?) {
            }

            override fun onAnimationResume(animation: Animator?) {
            }

        })
        /**
         * 3.3.4 ViewPropertyAnimator.withStartAction()/EndAction
         * 这两个方法是ViewPropertyAnimator的独有方法。它们和setListener()中
         * 回调的onAnimationStart()/onAnimationEnd()的区别在于：
         * - withStartAction()/withEndAction()是一次性的，在动画执行结束后就
         *   自动弃掉了，就算之后再重用ViewPropertyAnimator来做别的动画，用它们
         *   设置的回调也不会再被调用。而setListener()所设置的AnimatorListener
         *   是持续有效的，当动画重复执行时，回调总会被调用。
         * - withEndAction()设置的回调只有在动画正常结束时才会被调用，而在动画被
         *   取消时不会被执行。与AnimatorListener.onAnimationEnd()行为不一致。
         */

        val colorAnimator = ObjectAnimator.ofArgb(this,"color",Color.GREEN,Color.RED)
         colorAnimator.duration = 2000L
        val animatorSet = AnimatorSet()
        animatorSet.play(animator).with(colorAnimator)
        animatorSet.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.save()
            it.drawArc(
                    measuredWidth/4f,
                    measuredHeight/4f,
                    3*measuredWidth/4f,
                    3*measuredHeight/4f,
                    135f,
                    progress*2.7f,
                    false,
                    paint
            )
            it.restore()
            it.save()
            it.drawText(
                    "${progress.toInt()}%",
                    measuredWidth/2f-ResourceUtil.getDimen(context,R.dimen.sp_14),
                    measuredHeight/2f,
                    textPaint)
            it.restore()
        }
    }

    private val animatorUpdateListener = object :ValueAnimator.AnimatorUpdateListener{
        /**
         * 当动画属性更新时，此方法被调用。
         * 参数ValueAnimator是ObjectAnimator的父类，也是
         * ViewPropertyAnimator的内部实现。
         *
         * ValueAnimator有很多方法可以用，可以查看当前的动画完成度、
         * 当前的属性值等。
         */
        override fun onAnimationUpdate(animation: ValueAnimator?) {
        }

    }
    private val animatorListener = object :Animator.AnimatorListener{
        /**
         * 当动画开始执行时，此方法被调用
         */
        override fun onAnimationStart(animation: Animator?) {
        }

        /**
         * 当动画结束时，此方法被调用
         */
        override fun onAnimationEnd(animation: Animator?) {
        }

        /**
         * 当动画通过cancel()方法取消时，这个方法被调用
         */
        override fun onAnimationCancel(animation: Animator?) {
        }

        /**
         * 当动画通过setRepeatMode()/setRepeatCount()或repeat()
         * 方法重复执行时，这个方法被调用。
         * 由于ViewPropertyAnimator不支持重复，所以这个方法对ViewPropertyAnimator无效。
         */
        override fun onAnimationRepeat(animation: Animator?) {
        }

    }
}