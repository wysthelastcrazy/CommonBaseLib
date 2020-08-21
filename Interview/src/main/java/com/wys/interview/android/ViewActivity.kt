package com.wys.interview.android

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import com.wys.interview.R
import com.wys.interview.android.henCoder.view.FirstView
import com.wys.interview.javaBase.ThreadNote
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        val TAG = "ViewActivity"
        val s = javaClass.simpleName
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        ivAdv.setOnTouchListener { _: View, _: MotionEvent ->
            true
        }
        ivAdv.setOnClickListener(this)
        val view = FirstView(this)

        cannon_view.setImageDrawable(getDrawable(R.drawable.cannon))
        cannon_view.setOnClickListener(this)
        cannon_view.visibility = View.GONE

        ThreadNote().test3()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivAdv -> {
                Log.d(TAG,"onClick")
            }
            R.id.cannon_view ->{
                cannon_view.turnAngle(45f)
            }
        }
    }
}