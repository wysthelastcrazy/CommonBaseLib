package com.aixuexi.jetpack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aixuexi.jetpack.viewmodel.MyActivity
import kotlinx.android.synthetic.main.activity_jet_pack.*

class JetPackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_pack)

        btn_view_model.setOnClickListener {
            val intent = Intent(this,MyActivity::class.java)
            startActivity(intent)
        }
    }
}