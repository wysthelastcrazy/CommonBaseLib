package com.wys.interview.android.view

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.customview.widget.ViewDragHelper

/**
 * @author wangyasheng
 * @date 2020/8/7
 * @Describe:
 */
class DragViewGroup(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context?,attrs: AttributeSet?) : this(context,attrs,0)
    constructor(context: Context?): this(context,null)
    private val TAG = "DragViewGroup"
    private val mViewDragHelper: ViewDragHelper
    //固定（不能拖拽）View集合
    private val lockList: MutableList<View> = mutableListOf()
    //原始位置Point集合
    private val positions: MutableList<Point> = mutableListOf()
    private var touchFlag = false
    //被捕获view的初始left
    private var beforeX = 0
    //被捕获view的初始top
    private var beforeY = 0

    //ACTION_UP时的x坐标
    private var handPositionX = 0
    //ACTION_UP时的y坐标
    private var handPositionY = 0
    init {
        /**
         * ViewDragHelper
         * 构造私有，通过create()构造对象；
         * 第一个参数parent，也就是使用ViewDragHelper的View本身；
         * 第二个参数sensitivity，用来设置mTouchSlop，它的值越大mTouchSlop越小，越敏感；
         * 第三个参数callback
         */
        mViewDragHelper = ViewDragHelper.create(this,1.0f,object : ViewDragHelper.Callback(){

            /**
             * view的拖拽状态改变时触发
             * STATE_IDLE：未被拖拽
             * STATE_DRAGGING：正在被拖拽
             * STATE_SETTLING：被安放到一个位置中的状态
             */
            override fun onViewDragStateChanged(state: Int) {
                super.onViewDragStateChanged(state)
            }

            /**
             * 拖拽时（开始移动）触发
             * changedView：当前被拖拽的view
             * left：拖动时left坐标
             * top：拖动时top坐标
             * dx：拖拽时x轴偏移量
             * dy：拖拽时y轴偏移量
             */
            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
            }

            /**
             * view被捕获时触发（也就是按下），一般用于做准备初始化工作
             * capturedChild：捕获的view
             * activePointerId：按下手指的id，多指触控时会用到
             */
            override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
                super.onViewCaptured(capturedChild, activePointerId)
            }

            /**
             * view被放下时触发，一般用于收尾工作
             * releasedChild被放下的view
             * xvel：释放View的x轴方向上的加速度
             * yvel：释放View的y轴方向上的加速度
             */
            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                checkPosition(releasedChild)
                resetTouch()
            }

            /**
             * 边缘触摸时触发（需要开启边缘触摸），使用较少，一般不重写
             * edgeFlags：触摸的位置EDGE_LEFT,EDGE_TOP,EDGE_RIGHT,EDGE_BOTTOM
             * pointerId：按下手指的id，多指触控时会用到
             */
            override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
                super.onEdgeTouched(edgeFlags, pointerId)
            }

            /**
             * 是否开启边缘触摸，true代表开启，默认不开启，使用较少，一般不重写
             * edgeFlags：触摸的位置EDGE_LEFT,EDGE_TOP,EDGE_RIGHT,EDGE_BOTTOM
             */
            override fun onEdgeLock(edgeFlags: Int): Boolean {
                return super.onEdgeLock(edgeFlags)
            }

            /**
             * 边缘触摸时触发（需开启边缘触摸），使用较少，一般不重写
             * edgeFlags：触摸的位置EDGE_LEFT,EDGE_TOP,EDGE_RIGHT,EDGE_BOTTOM
             * pointerId：按下手指的id，多指触控时会用到
             */
            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                super.onEdgeDragStarted(edgeFlags, pointerId)
            }

            /**
             * 寻找当前触摸点下的子View时会调用此方法，寻找到的View会提供
             * 给tryCaptureViewForDrag()来尝试捕获。
             * 如果需要改变子view的遍历查询顺序可改写此方法，例如让下层的View优先
             * 于上层的View被选中。
             * 使用较少，一般不重写。
             */
            override fun getOrderedChildIndex(index: Int): Int {
                return super.getOrderedChildIndex(index)
            }

            /**
             * 尝试捕获被拖拽的view，如果返回ture代表可以被拖拽，
             * 返回false代表不可以被拖拽。
             * child：被拖拽的view
             * pointerId：多指操作使用
             * 一般判断很多view其中那些是可以移动时使用
             */
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                if (lockList.contains(child))return false
                if (touchFlag) return false

                beforeX = child.left
                beforeY = child.top
                touchFlag = true
                addPosition(beforeX,beforeY)
                return true
            }

            /**
             * 返回view在水平方向的位置
             * left：当前被拖拽的view要移动到的left值
             * dx：移动的偏移量
             * 返回0则无法移动，通常直接返回left
             * 一般必须重写此方法返回left
             */
            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                val position = width - child.width
                return if (left > position) position else if(left < 0) 0 else left
            }

            /**
             * 返回view在竖直方向的位置
             * top：当前被拖拽的view要移动到的top值
             * dy：移动的偏移量
             * 但会0则无法移动，通常直接返回top
             * 一般必须重写此方法返回top
             */
            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                val position = height - child.height
                return if (top > position) position else if(top < 0) 0 else top
            }
        })
    }
    private fun addPosition( left: Int, top: Int){
        val point = Point()
        point.x = left
        point.y = top
        if (!positions.contains(point))
            positions.add(point)
    }

    /**
     * 检测当前view是否到达指定的位置
     */
    private fun checkPosition(releasedChild: View) : Boolean{
        val rect = Rect()
        lockList.forEach {
            rect.set(it.left,it.top,it.left + it.width,it.top + it.height)
            if (rect.contains(handPositionX,handPositionY)&&releasedChild.tag == it.tag){
                //放手时，如果event坐标在lockView的范围内，则将
                //当前拖拽的view放在指定位置，并矫正位置
                //当前拖拽的view放在指定位置后，也不再能拖动
                lockList.add(releasedChild)
                gotoTargetPosition(releasedChild,it)
                return true
            }
        }
        setViewGoBack(releasedChild)
        return false
    }

    /**
     * 添加lockView，即不能拖动的childView
     */
    public fun addLockView(lockChild:View?){
        if (lockChild == null) return
        lockList.add(lockChild)
        addView(lockChild)
    }

    /**
     * 将拖拽的[dragChild]移动到指定的[targetChild]所在的位置
     */
    private fun gotoTargetPosition(dragChild: View,targetChild: View){

        //settleCapturedViewAt()此方法以松手前的速度为初速度，让捕获到的view即dragChild自动
        //滚动到指定的位置，此方法只能在callback的onViewReleased()中调用。
        mViewDragHelper.settleCapturedViewAt(targetChild.x.toInt(),targetChild.y.toInt())
        invalidate()
        //保证位置一样
        dragChild.layoutParams = targetChild.layoutParams
    }

    /**
     * 未拖拽至指定位置是，自动返回之前的位置
     */
    private fun setViewGoBack(releasedChild: View){
        if (positions.contains(Point(beforeX,beforeY))){
            mViewDragHelper.settleCapturedViewAt(beforeX,beforeY)
            invalidate()
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL
                -> if (touchFlag){
                handPositionX = event.x.toInt()
                handPositionY = event.y.toInt()
                touchFlag = false
            }

        }
        mViewDragHelper.processTouchEvent(event!!)
        return true
    }
    //将事件是否拦截的判断交给mViewDragHelper处理
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mViewDragHelper.shouldInterceptTouchEvent(ev)
    }

    /**
     * 处理完拖拽判断逻辑后，重置相关属性
     */
    private fun resetTouch(){
        touchFlag = false
        handPositionX = 0
        handPositionY = 0
        beforeX = 0
        beforeY = 0
    }

    /**
     * 必须要重写此方法，否则mViewDragHelper.settleCapturedViewAt()不其作用
     * 因为在ViewDragHelper中使用Scroller
     */
    override fun computeScroll() {
        super.computeScroll()
        //使用continueSettling(true)判断拖拽是否完成
        if (mViewDragHelper.continueSettling(true)){
            invalidate()
        }
    }
}