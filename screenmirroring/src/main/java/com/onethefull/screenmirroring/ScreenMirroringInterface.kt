package com.onethefull.screenmirroring

import android.content.Context

interface ScreenMirroringInterface {

    fun init(appContext : Context)

    fun start(appContext: Context)

    fun stop(appContext : Context)
}