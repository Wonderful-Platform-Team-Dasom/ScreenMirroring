package com.onethefull.screenmirroring

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.Actions
import com.onethefull.screenmirroring.ScreenMirroringConstants.Companion.MSG_NOTIFICATION
import java.lang.Exception
import java.util.*

class ScreenMirroringImpl : ScreenMirroring(){

    val TAG  = "ScreenMirroringImpl"
    override fun init(appContext : Context) {
        ScreenMirroringManager.getInstance().initialize(appContext)
        FirebaseUserActions.getInstance(appContext).start(getAction())

        LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(MSG_NOTIFICATION).apply {
            putExtra("message",ScreenMirroringResponse(ScreenMirroringResponseCode.SUCCESS, ScreenMirroringResponseState.INIT
            ))
        })
    }

    override fun start(appContext : Context) {
        try{
            appContext.startActivity(Intent("android.settings.CAST_SETTINGS"))



         //   ScreenMirroringService.startService(appContext)
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(MSG_NOTIFICATION).apply {
                putExtra("message", ScreenMirroringResponse(ScreenMirroringResponseCode.SUCCESS, ScreenMirroringResponseState.START))
            })
        }catch (e : Exception){
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(MSG_NOTIFICATION).apply {
                putExtra("message", ScreenMirroringResponse(ScreenMirroringResponseCode.ERROR_EMPTY_PARAMETERS, ScreenMirroringResponseState.START))
            })
        }

    }



    override fun stop(appContext : Context) {
        var activityManager : ActivityManager = appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager


        var usageStatsManager = appContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, Calendar.getInstance().timeInMillis, System.currentTimeMillis())
 /*       var appList2 : List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServiceControlPanel(.)

        for(list in appList2){
            Log.d(TAG,  "${list.taskInfo}::")

        }*/


        Log.d(TAG,  "SIZE :: ${queryUsageStats.size}")

        for(app_list in queryUsageStats){
            Log.d(TAG,  "${app_list.packageName}::")

            //if(app_list.equals("com.samsung.android.smartmirroring"))

        }

        FirebaseUserActions.getInstance(appContext).end(getAction())
        LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(MSG_NOTIFICATION).apply {
            putExtra("message", ScreenMirroringResponse(ScreenMirroringResponseCode.SUCCESS, ScreenMirroringResponseState.STOP))
        })
    }

    // After
    fun getAction(): Action {
        return Actions.newView("Main Page", Uri.parse("http://").toString())
    }
}