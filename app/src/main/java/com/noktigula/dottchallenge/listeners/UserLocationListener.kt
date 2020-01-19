package com.noktigula.dottchallenge.listeners

import android.content.Context
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.LocationServices
import com.noktigula.dottchallenge.loge

class UserLocationListener(
    private val context: Context,
    private val callback: (Location)->Unit
    ) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start(){
        loge("Location listener started")
        connect()
    }

    private fun connect() {
//      TODO("Request permission to access location")
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener(callback)
            .addOnFailureListener { loge("Failed to receive location: ${it.message}") }
    }
}