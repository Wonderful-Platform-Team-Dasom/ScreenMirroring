package com.onethefull.mirroring

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.onethefull.mirroring.databinding.ActivityMainBinding
import com.onethefull.screenmirroring.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity(), ScreenMirroringMessagingInterface {

    val TAG = "MainActivity"
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1

    private lateinit var binding: ActivityMainBinding

    private var mService : MainService ?=null
    var service : Intent?=null
    var isBound :Boolean = false
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        service = Intent(this, MainService::class.java)

        binding.btnTest.setOnClickListener {
            checkPermission()
            //startActivity(Intent(this, FloatingActivity::class.java))
        }

        ScreenMirroringManager.getInstance().addListener(this)
        ScreenMirroring.getInstance().init(this)

 /*       var sec = 1
        timer(period = 1000,initialDelay = 1000){
            if(sec == 5){
                ScreenMirroring.getInstance().start(this@MainActivity)

            }
            sec++
        }*/
/*
        var stopI = intent
      isBound =  stopI.getBooleanExtra("STOP_SERVICE",false)

        if(isBound){
            onStop()
        }*/
    }

    override fun responseMessage(message: String) {
        val response : ScreenMirroringResponse = gson.fromJson(message, ScreenMirroringResponse::class.java)

        when(response.state){
            ScreenMirroringResponseState.INIT ->{
                Log.d(TAG, "ScreenMirroringResponseState INIT")
            }
            ScreenMirroringResponseState.START ->{
                when(response.code){
                    ScreenMirroringResponseCode.SUCCESS->{
                        Log.d(TAG, "ScreenMirroringResponseCode SUCCESS")
                        checkPermission()
                    }
                    ScreenMirroringResponseCode.ERROR_FAIL ->{
                        Log.d(TAG, "ScreenMirroringResponseCode ERROR_FAIL")
                    }
                }
            }
            ScreenMirroringResponseState.STOP ->{
                Log.d(TAG, "ScreenMirroringResponseState STOP")
               // ScreenMirroringService.stopService(this)

                MainService.stopService(this)

            }
        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {

                Log.d(TAG, "checkPermission $isBound")

        /*        if(!isBound) {
                    isBound =  bindService(service, mConnection, Context.BIND_AUTO_CREATE)
                }*/
               // MainService.startService(this)
                ScreenMirroringService.startService(this)
               //startService(Intent(this@MainActivity, MainService::clasgbs.java))
            }
        } else {
         //   startService(Intent(this@MainActivity, MainService::class.java))
          //  MainService.startService(this)
            ScreenMirroringService.startService(this)


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")

       // LocalBroadcastManager.getInstance(this).registerReceiver( mMessageReceiver,  IntentFilter("custom-event-name"))

    }




    override fun onPause() {
        super.onPause()
     //   LocalBroadcastManager.getInstance(this).unregisterReceiver( mMessageReceiver);

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var id = item.itemId

        if (id == R.id.action_fav) {
            //startActivity(Intent("android.settings.CAST_SETTINGS"))
            //checkPermission()


            ScreenMirroring.getInstance().start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart ")

        ScreenMirroring.getInstance().init(this)
    }

    override fun onStop() {
        Log.d(TAG, "onStop ")

        ScreenMirroring.getInstance().stop(this)
        super.onStop()
    }
}

/*    var mConnection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected")

            var binder : MainService.MainServiceBinder = service as MainService.MainServiceBinder
            mService = binder.getSerivce()
            mService?.registerCallback(mCallback)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")

            //mService = null
        }
    }
    var mCallback = object : MainService.ICallback{
        override fun onStopEvent() {
            Log.d(TAG, "stopEvent $isBound")
            if(isBound){
              //  unbindService(mConnection)
                isBound = false

            }

            //stopService(Intent(this@MainActivity, MainService::class.java))
        }
    }

    var mMessageReceiver = object :BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                var message = intent?.getStringExtra("message")
                Log.d(TAG,"mMessageReceiver  $message")

                if(message=="true"){
                    onStop()
                }
            }
        }
*/