package com.onethefull.screenmirroring

abstract class ScreenMirroring  protected constructor() : ScreenMirroringInterface{

    companion object{
        @Volatile private var instance : ScreenMirroring ?=null
        @JvmStatic fun getInstance() : ScreenMirroring =
            instance ?: synchronized(this){
                instance ?: ScreenMirroringImpl().also {
                    instance = it
                }
            }
    }
}