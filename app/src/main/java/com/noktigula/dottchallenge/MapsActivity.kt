package com.noktigula.dottchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

import com.google.android.gms.maps.model.LatLng
import com.noktigula.dottchallenge.listeners.UserLocationListener
import com.noktigula.dottchallenge.viewmodels.MapViewModel

class MapsActivity : AppCompatActivity() {
    private val mapViewModel : MapViewModel by viewModels()
    private lateinit var locationListener : UserLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        locationListener = UserLocationListener(this){ userLocation ->
            mapViewModel.location.value = LatLng(userLocation.latitude, userLocation.longitude)
        }
        lifecycle.addObserver(locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationListener.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
