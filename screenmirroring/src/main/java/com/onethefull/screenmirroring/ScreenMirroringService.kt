package com.onethefull.screenmirroring

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ScreenMirroringService : Service(){

    val TAG = "ScreenMirroringService"

    var wm: WindowManager? = null
    var mView: View? = null

    var moveX = 0f
    var moveY = 0f
    private lateinit var mLayoutParams: WindowManager.LayoutParams

    var appContext : Context ?=null
    companion object{
        fun startService(context: Context){
            val startIntent = Intent(context, ScreenMirroringService::class.java)
            context.startService(startIntent)

        }

        fun stopService(context: Context){
            val stopIntent = Intent(context, ScreenMirroringService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(ScreenMirroringConstants.MSG_NOTIFICATION).apply {
            putExtra("message", ScreenMirroringResponse(ScreenMirroringResponseCode.SUCCESS, ScreenMirroringResponseState.START))
        })
    }

    override fun onBind(intent: Intent?): IBinder?=null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        val inflate = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        wm = getSystemService(WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams = WindowManager.LayoutParams(
                300,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            )
        } else {
            mLayoutParams = WindowManager.LayoutParams(
                300,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR),
                PixelFormat.TRANSLUCENT
            )
        }
        mLayoutParams.gravity = Gravity.LEFT or Gravity.TOP
        mView = inflate.inflate(R.layout.view_in_service, null)

        var bt : ImageButton? = mView?.findViewById(R.id.bt)
        var tv : TextView? = mView?.findViewById(R.id.textView)
        var bg : RelativeLayout? = mView?.findViewById(R.id.rl_bg)
        bt?.setOnClickListener{

            bt?.setImageResource(R.mipmap.ic_launcher_round)
            tv?.text = "on click!!"

            sendMessage()
            stopEvent()

        }

        bg?.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveX = v.x - event.rawX
                    moveY = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + moveX)
                        .y(event.rawY + moveY)
                        .setDuration(0).start()
                }
            }

            true
        }
        wm?.addView(mView, mLayoutParams)

        return super.onStartCommand(intent, flags, startId)
    }


    fun sendMessage(){
        Log.d(TAG,"sendMessage")
        var intent = Intent("custom-event-name")
        intent.putExtra("message","true")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    fun stopEvent(){
        Log.d(TAG, "stopEvent")
        wm?.removeView(mView)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopEvent()
    }
}