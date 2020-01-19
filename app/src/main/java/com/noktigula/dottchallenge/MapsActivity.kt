package com.noktigula.dottchallenge

import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.noktigula.dottchallenge.listeners.UserLocationListener
import com.noktigula.dottchallenge.viewmodels.LocationViewModel

class MapsActivity : AppCompatActivity() {
    private val locationViewModel : LocationViewModel by viewModels()
    private lateinit var locationListener : UserLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        locationListener = UserLocationListener(this){ userLocation ->
            locationViewModel.location.value = LatLng(userLocation.latitude, userLocation.longitude)
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
