package com.wys.commonbaselib.jetpack.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wys.commonbaselib.R
import com.wys.commonbaselib.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModule = ViewModelProvider(this).get(LoginViewModule::class.java)
// Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
//        val model : LoginViewModule by viewModels()
        viewModule.getUsers().observe(this, Observer<List<String>> {
            //update UI
        })


        lifecycle.addObserver(LoginModule())
        val binding: ActivityLoginBinding
                = DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.user = User("dnd", 18)
    }

}
