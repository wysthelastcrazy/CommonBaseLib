package com.wys.commonbaselib

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.wys.commonbaselib.activity.*
import com.wys.commonbaselib.kotlin.Day03
import com.wys.commonbaselib.music.MusicListActivity
import com.wys.commonbaselib.zxing.activity.CaptureActivity

/**
 * @author wangyasheng
 */
class MainActivity : AppCompatActivity() {
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        )
        requestPermissions(permissions, 100)
        val day03 = Day03()
        day03.day03Enter()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_download -> {
                val intent = Intent(this, DownloadActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_m3u8 -> {
                intent = Intent(this, M3U8DownloadActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_music -> {
                intent = Intent(this, MusicListActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_player -> {
                intent = Intent(this, VideoPlayerActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_zxing -> {
                intent = Intent(this, CaptureActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_video_list -> {
                intent = Intent(this, VideoListActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_photo_list -> {
                intent = Intent(this, PhotoListActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_mediaRecorder -> {
                intent = Intent(this, MediaRecorderActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_jetpack -> {
//                intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
            }
            R.id.btn_net -> {
                intent = Intent(this, RequestActivity::class.java)
                startActivity(intent)
            }
        }
    }
}