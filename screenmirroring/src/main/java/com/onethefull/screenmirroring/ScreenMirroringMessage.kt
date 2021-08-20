package com.onethefull.screenmirroring

import android.os.Parcelable

abstract class ScreenMirroringMessage : Parcelable{

    abstract val header : ScreenMirroringHeader

    var bundle : Map<String, Any> = mapOf()

    data class ScreenMirroringHeader(val type : ScreenMirroringType)

    enum class ScreenMirroringType(val value : String){
        EVENT("Event")
    }
}