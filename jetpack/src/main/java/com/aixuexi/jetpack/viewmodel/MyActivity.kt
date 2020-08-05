package com.aixuexi.jetpack.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aixuexi.jetpack.R

class MyActivity : AppCompatActivity() {
    private val TAG = "MyActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        val model: MyViewModel  = ViewModelProvider(this).get(MyViewModel::class.java)
        model.getUsers().observe(this, Observer {
            users -> Log.d(TAG,"update UI")
            val d = users
        })

        Handler().postDelayed(Runnable {
            model.updateUser()
        },1000)
    }
}