package com.onethefull.screenmirroring

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.Actions
import com.onethefull.screenmirroring.ScreenMirroringConstants.Companion.MSG_NOTIFICATION
import java.lang.Exception

class ScreenMirroringImpl : ScreenMirroring(){

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

            LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(MSG_NOTIFICATION).apply {
                putExtra("mesaage", ScreenMirroringResponse(ScreenMirroringResponseCode.SUCCESS, ScreenMirroringResponseState.START))
            })
        }catch (e : Exception){
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(MSG_NOTIFICATION).apply {
                putExtra("mesaage", ScreenMirroringResponse(ScreenMirroringResponseCode.ERROR_EMPTY_PARAMETERS, ScreenMirroringResponseState.START))
            })
        }

    }

    override fun stop(appContext : Context) {
        FirebaseUserActions.getInstance(appContext).end(getAction())
        LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(MSG_NOTIFICATION).apply {
            putExtra("mesaage", ScreenMirroringResponse(ScreenMirroringResponseCode.SUCCESS, ScreenMirroringResponseState.STOP))
        })
    }

    // After
    fun getAction(): Action {
        return Actions.newView("Main Page", Uri.parse("http://").toString())
    }
}