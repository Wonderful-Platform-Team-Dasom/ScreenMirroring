package com.onethefull.screenmirroring

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.onethefull.screenmirroring.ScreenMirroringConstants.Companion.MSG_NOTIFICATION

class ScreenMirroringManager private constructor(){

    companion object{
        @Volatile private var instance : ScreenMirroringManager ?= null
        @JvmStatic
        fun getInstance(): ScreenMirroringManager =
            instance ?: synchronized(this){
                instance ?: ScreenMirroringManager().also { instance = it }
            }
    }

    var context : Context?=null
    var broadcastReceiver : BroadcastReceiver?=null
    var listenerList : ArrayList<ScreenMirroringMessagingInterface> = ArrayList()
    fun initialize(context : Context){
        this.context = context
        broadcastReceiver = object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    receiveMessage(it)
                }
            }
        }

        broadcastReceiver?.let{
            LocalBroadcastManager.getInstance(context)
                .registerReceiver(it, IntentFilter(MSG_NOTIFICATION))
        }
    }

    fun addListener(listener: ScreenMirroringMessagingInterface) {
        listenerList.add(listener)
    }

    fun removeListener(listener: ScreenMirroringMessagingInterface) {
        listenerList.remove(listener)
    }
    private fun receiveMessage(intent : Intent){
        intent.extras?.get("message")?.let {

            val gson = Gson()
            if(it is ScreenMirroringResponse){
                for(listener in listenerList){
                    listener.responseMessage(gson.toJson(it))
                }
            }
        }
    }
}