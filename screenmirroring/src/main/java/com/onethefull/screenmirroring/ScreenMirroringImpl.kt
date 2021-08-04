package com.onethefull.screenmirroring

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.Actions

class ScreenMirroringImpl : ScreenMirroring(){

    override fun init(appContext : Context) {
        FirebaseUserActions.getInstance(appContext).start(getAction());
    }

    override fun start(appContext : Context) {
        appContext.startActivity(Intent("android.settings.CAST_SETTINGS"))
    }

    override fun stop(appContext : Context) {
        FirebaseUserActions.getInstance(appContext).end(getAction());
    }

    // After
    fun getAction(): Action {
        return Actions.newView("Main Page", Uri.parse("http://").toString())
    }
}