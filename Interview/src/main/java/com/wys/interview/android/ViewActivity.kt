package com.wys.interview.android

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import com.wys.interview.R
import com.wys.interview.android.bean.Man
import com.wys.interview.android.henCoder.view.FirstView
import com.wys.interview.android.henCoder.view.evalutor.HsvEvaluator
import com.wys.interview.android.henCoder.view.evalutor.PointFEvaluator
import com.wys.interview.javaBase.ThreadNote
import com.wys.interview.leetcode.MinStack
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        val TAG = javaClass.simpleName
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
//        ivAdv.setOnClickListener(this)
//        cannon_view.setImageDrawable(getDrawable(R.drawable.cannon))
//        cannon_view.setOnClickListener(this)
//        cannon_view.visibility = View.GONE

        ThreadNote().test4()
        sportView.startAnim(0f,65f)
        val animator = ObjectAnimator.ofInt(
                sixthView,
                "color",
                Color.parseColor("#ff00ffff"),
                Color.parseColor("#ff00ff00")
        )
        animator.duration = 2000L
        animator.setEvaluator(HsvEvaluator())
//        animator.start()
        val pointAnimator = ObjectAnimator.ofObject(
                sixthView,
                "position",
                PointFEvaluator(),
                PointF(0f,0f),
                PointF(1f,1f)

        )
        pointAnimator.duration = 2000L

        val animatorSet = AnimatorSet()
        animatorSet.play(animator).with(pointAnimator)
        animatorSet.start()

        val man = Man()
        val minStack = MinStack()
        minStack.push(2)
        minStack.push(-2)
        minStack.push(3)
        Log.d("ViewActivity1111","min = ${minStack.getMin()}")
        Log.d("ViewActivity1111","top = ${minStack.top()}")
        minStack.pop()
        Log.d("ViewActivity1111","min = ${minStack.getMin()}")
        Log.d("ViewActivity1111","top = ${minStack.top()}")
        minStack.pop()
        Log.d("ViewActivity1111","min = ${minStack.getMin()}")
        Log.d("ViewActivity1111","top = ${minStack.top()}")
        minStack.pop()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        return super.dispatchTouchEvent(ev)
        Log.d(TAG,"[dispatchTouchEvent]+++++++++++++++++++")
        return onTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG,"[onTouchEvent]+++++++++++++++++++")
        return super.onTouchEvent(event)
    }
    override fun onClick(v: View?) {
//        when(v?.id){
//            R.id.ivAdv -> {
//                Log.d(TAG,"onClick")
//            }
//            R.id.cannon_view ->{
//                cannon_view.turnAngle(45f)
//            }
//        }
    }
}