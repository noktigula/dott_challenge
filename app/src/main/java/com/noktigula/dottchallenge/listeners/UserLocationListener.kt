package com.noktigula.dottchallenge.listeners

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.LocationServices
import com.noktigula.dottchallenge.loge

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import androidx.core.app.ActivityCompat

private val PERMISSION_REQUEST_CODE = 42

class UserLocationListener(
    private val activity: Activity,
    private val callback: (Location)->Unit
    ) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start(){
        loge("Location listener started")
        connect()
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        connect()
    }

    private fun connect() {
        withPermission(ACCESS_FINE_LOCATION) {
            LocationServices.getFusedLocationProviderClient(activity)
                .lastLocation
                .addOnSuccessListener(callback)
                .addOnFailureListener { loge("Failed to receive location: ${it.message}") }
        }
    }

    private fun withPermission(permission:String, callback:()->Unit) {
        if (activity.permissionGranted(permission)) {
            callback()
        } else {
            ActivityCompat.requestPermissions(activity,
                arrayOf(permission),
                PERMISSION_REQUEST_CODE)
        }
    }
}

private fun Context.permissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


