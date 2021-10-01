package com.onethefull.mirroring.floatingback

import android.app.ActivityManager
import android.app.Instrumentation
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.Button
import com.onethefull.mirroring.R

class AppTopBackService : Service() {
    private val TAG = "AppTopService"

    private var m_View: View? = null
    private var m_WindowManager: WindowManager? = null
    private var m_Params: WindowManager.LayoutParams? = null

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val mInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        Log.d("WLog", "AppTopService")

        m_View = mInflater.inflate(R.layout.app_top_view, null)
        m_View!!.setOnClickListener {
            onClickBackBtn(it)
        }
        m_Params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        m_Params!!.x = 30
        m_Params!!.gravity = Gravity.BOTTOM or Gravity.START

        // 정렬
        m_WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        m_WindowManager!!.addView(m_View, m_Params)
    }

    override fun onDestroy() {
        super.onDestroy()

        /*
        //생성된 레이아웃 제거
        try {
            var packageName = (application).runningPackageName
            Log.d("AppTopService", packageName)
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.killBackgroundProcesses(packageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        */

        m_WindowManager!!.removeView(m_View)
        m_WindowManager = null
    }

    fun onClickBackBtn(v: View) {
        //서비스 종료
        run {
            Thread(Runnable {
                Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            }).start()
        }
    }
}