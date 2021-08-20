package com.onethefull.mirroring

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.onethefull.mirroring.databinding.ActivityFloatingBinding

class FloatingActivity : AppCompatActivity() {


    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1
    val TAG = "Floating"


    private lateinit var binding : ActivityFloatingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFloatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermission()
    }


    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop")
    }
    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
    }


    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                Log.e("myLog", "startService")
                startService()
            }
        } else {
            startService()
        }
    }

    fun startService() {
        val floatingService = Intent(applicationContext, FloatingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(floatingService)
        }
    }

    fun stopService() {
        stopService(Intent(applicationContext, FloatingService::class.java))
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("myLog", "startService $resultCode")

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리
                finish()
            } else {
                startService()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        stopService()
    }
}
