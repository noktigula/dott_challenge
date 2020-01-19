package com.noktigula.dottchallenge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

private const val TAG = "Dott"

fun <T> MutableLiveData<T>.default(initialValue:T) : MutableLiveData<T> {
    this.value = initialValue
    return this
}

fun loge(message:String) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, message)
    }
}

fun LatLng.simpleString() : String = "${latitude},${longitude}"