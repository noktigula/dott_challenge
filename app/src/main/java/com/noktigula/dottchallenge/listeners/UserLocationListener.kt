package com.noktigula.dottchallenge.listeners

import android.content.Context
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class UserLocationListener(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val callback: (Location)->Unit
    ) : LifecycleObserver {

    private var enabled = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start(){
        if (!enabled) {
            return
        }

        connect()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disconnect()
    }

    private fun connect() {
        TODO("Request permission to access location")
        TODO("Connect to LocationServices in a callback")
        TODO("Feed LocationViewModel with updated location ONCE")
    }

    private fun disconnect() {

    }
}