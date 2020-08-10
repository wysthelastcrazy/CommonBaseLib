package com.wys.interview.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.wys.interview.R
import com.wys.interview.android.view.FirstView
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        val TAG = "ViewActivity"
        val s = javaClass.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        ivAdv.setOnTouchListener { _: View, _: MotionEvent ->
            true
        }
        ivAdv.setOnClickListener(this)
        val view = FirstView(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
        when(v?.id){
            R.id.ivAdv -> {
                Log.d(TAG,"onClick")
            }
        }
    }
}