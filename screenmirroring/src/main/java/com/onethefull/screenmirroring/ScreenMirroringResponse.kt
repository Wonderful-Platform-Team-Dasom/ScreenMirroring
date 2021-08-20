package com.onethefull.screenmirroring

import android.os.Parcel
import android.os.Parcelable

enum  class ScreenMirroringResponseState{
    INIT,
    START,
    STOP
}

enum class ScreenMirroringResponseCode(val msg : String){
    SUCCESS("SUCCESS"),
    ERROR_FAIL("ERROR_FAIL"),
    ERROR_EMPTY_PARAMETERS("ERROR_EMPTY_PARAMETERS")

}
data class ScreenMirroringResponse(val code : ScreenMirroringResponseCode, val state : ScreenMirroringResponseState) : ScreenMirroringMessage(), Parcelable{
    override val header: ScreenMirroringHeader
        get() = ScreenMirroringHeader(ScreenMirroringType.EVENT)

    constructor(source : Parcel) :this(
        ScreenMirroringResponseCode.values()[source.readInt()],
        ScreenMirroringResponseState.values()[source.readInt()]
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest){
        writeInt(code.ordinal)
    }

    companion object{
        @JvmField
        val CREATOR : Parcelable.Creator<ScreenMirroringResponse> =
            object : Parcelable.Creator<ScreenMirroringResponse>{
                override fun createFromParcel(source: Parcel): ScreenMirroringResponse =
                    ScreenMirroringResponse(source)

                override fun newArray(size: Int): Array<ScreenMirroringResponse?> = arrayOfNulls(size)
            }
    }
}