package com.onethefull.mirroring

import android.app.Service
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.onethefull.screenmirroring.ScreenMirroring
import com.onethefull.screenmirroring.ScreenMirroringService


class MainService : Service(){

    val TAG = "MainService"
    interface ICallback{
        fun onStopEvent()
    }

    inner class MainServiceBinder : Binder(){
        fun getService() : MainService{
            return this@MainService
        }
    }

    private var mBinder : IBinder =MainServiceBinder()

    private var mCallback : ICallback? =null
    var wm: WindowManager? = null
    var mView: View? = null

    var moveX = 0f
    var moveY = 0f
    private lateinit var mLayoutParams: WindowManager.LayoutParams


    companion object{
        fun startService(context: Context){
            val startIntent = Intent(context, MainService::class.java)
            context.startService(startIntent)
        }

        fun stopService(context : Context){
            val stopIntent = Intent(context, MainService::class.java)
            context.stopService(stopIntent)
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate")


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        val inflate = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
     /*   val params = WindowManager.LayoutParams( *//*ViewGroup.LayoutParams.MATCH_PARENT*//*
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT)
*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams = WindowManager.LayoutParams(
                    300,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    /*  or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                      or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                      or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,*/
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

           // sendMessage()
            //stopEvent()

            ScreenMirroring.getInstance().stop(this)
            //   stopSelf()
         //   mCallback?.onStopEvent()

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

    fun registerCallback(iCallback: ICallback){
        mCallback = iCallback
    }


    fun stopEvent(){
        Log.d(TAG, "stopEvent")
         wm?.removeView(mView)

        //ScreenMirroringService.stopService(this)
        stopService(this)

/*
            if(mView != null){
                wm?.removeView(mView)
                mView = null
            }
*/

    }

    override fun onDestroy() {
        super.onDestroy()
        stopEvent()
    }
}